public class CoolBool extends CoolObject {
    private final boolean value;

    public CoolBool(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof CoolBool && this.value == ((CoolBool) other).value;
    }
}
