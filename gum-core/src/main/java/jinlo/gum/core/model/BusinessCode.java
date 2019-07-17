package jinlo.gum.core.model;

import java.io.Serializable;

/**
 * 业务编码
 * @see BusinessCodeParser
 */
public final class BusinessCode implements Serializable {

    private String code;

    protected BusinessCode(String code) {
        if (code == null||code.isEmpty()) {
            throw new IllegalArgumentException("empty code");
        }

        this.code = code;
    }

    public static BusinessCode of(String code){
        return new BusinessCode(code);
    }

    public String code() {
        return this.code;
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
