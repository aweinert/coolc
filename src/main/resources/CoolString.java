public class CoolString extends CoolObject {
    private final String value;

    public CoolString(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof CoolString && this.value.equals(((CoolString) other).value);
    }
}
