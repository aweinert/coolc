public class CoolString extends CoolObject {
    private final String value;

    public CoolString(String value) {
        this.value = value;
    }

    public CoolInt length() {
        return new CoolInt(this.value.length());
    }

    public CoolString concat(CoolString other) {
        return new CoolString(this.value + other.value);
    }

    public CoolString substr(CoolInt i, CoolInt l) {
        return new CoolString(this.value.substring(i.getValue(), i.getValue() + l.getValue()));
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof CoolString && this.value.equals(((CoolString) other).value);
    }
}
