public class CoolInt extends CoolObject {
    private final int value;

    public CoolInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof CoolInt && this.value == ((CoolInt) other).value;
    }
}
