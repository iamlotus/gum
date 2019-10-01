package jinlo.gum.core.spec;


import java.util.Objects;


public abstract class Spec {


    //Id, full qualified class name
    private String code;

    private String name;

    private String description;

    public Spec(final String code) {
        this(code, null);
    }

    //use constructor to constraint code is not empty
    public Spec(final String code, final String name) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("code can not be empty");
        }

        this.code = code;
        this.name = (name == null || name.isEmpty()) ? code : name;
    }


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spec spec = (Spec) o;
        return code.equals(spec.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("[").append(getClass().getSimpleName())
                .append(" code=\"").append(code)
                .append("\"");
        if (!code.equals(name)) {
            sb.append(", name=\"").append(name).append("\"");
        }
        sb.append("]");
        return sb.toString();

    }

}
