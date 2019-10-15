package jinlo.gum.core.model;

import jinlo.gum.core.annotation.Extension;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * Business code is used to locate the implementations of {@link Extension} which has a hierarchical structure by dot to
 * represent inheritance diagram of an organization. It is encouraged to organize your business code in some hierarchical
 * format like {@code "group.company.businessUnit.businessGroup.business.subBusiness"}. But the hierarchical structure is
 * not used by now.
 *
 * @see BusinessCodeParser
 */
public final class BusinessCode implements Serializable {

    private String code;

    private static final Pattern CODE_PATTERN = Pattern.compile("^[^.]+((.)[^.]+)*$|^$");

    private BusinessCode(String code) {
        if (code == null) {
            throw new NullPointerException();
        }

        if (!CODE_PATTERN.matcher(code).matches()) {
            throw new IllegalArgumentException("\"" + code + "\" is not valid code");
        }

        this.code = code;
    }

    public static BusinessCode root() {
        return new BusinessCode("");
    }

    public static BusinessCode of(String code) {
        return new BusinessCode(code);
    }

    /**
     * Find ancestor of specified codes
     *
     * @param codes not null
     * @return
     */
    public static BusinessCode ancestorOf(BusinessCode... codes) {

        if (codes == null || codes.length == 0) {
            throw new IllegalArgumentException();
        }

        int size = codes.length;

        BusinessCode result = codes[0];

        for (int i = 0; (!result.isRoot() && (i + 1 < size)); i++) {
            result = codes[i].join(codes[i + 1]);
        }

        return result;
    }

    public boolean isRoot() {
        return code.isEmpty();
    }

    public String code() {
        return this.code;
    }

    /**
     * find the same part of code (split by dot).
     * <pre>
     *  assertEquals((BusinessCode.root(),BusinessCode.of("a").join(BusinessCode.root()));
     *  assertEquals((BusinessCode.of("a"),BusinessCode.of("a").join(BusinessCode.of("a")));
     *  assertEquals((BusinessCode.of("a.b"),BusinessCode.of("a.b").join(BusinessCode.of("a.b")));
     *  assertEquals((BusinessCode.of("a.b"),BusinessCode.of("a.b.c").join(BusinessCode.of("a.b")));
     *  assertEquals((BusinessCode.of("a"),BusinessCode.of("a.b.c").join(BusinessCode.of("a")));
     *  assertNull(BusinessCode.of("a.b.c").join(BusinessCode.of("b"))));
     * </pre>
     *
     * @param other
     * @return
     */
    public BusinessCode join(BusinessCode other) {
        Objects.requireNonNull(other);
        StringJoiner result = new StringJoiner(".");
        String[] srcParts = this.code().split("\\.");
        String[] otherParts = other.code().split("\\.");
        int srcLen = srcParts.length;
        int otherLen = otherParts.length;
        for (int i = 0; i < srcLen && i < otherLen; i++) {
            if (srcParts[i].equals(otherParts[i])) {
                result.add(srcParts[i]);
            } else {
                break;
            }
        }
        return result.length() == 0 ? BusinessCode.root() : BusinessCode.of(result.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BusinessCode)) return false;
        BusinessCode that = (BusinessCode) o;
        return this.code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
