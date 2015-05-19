public class CoolObject {
    public CoolObject abort() {
        System.exit(1);
        return null;
    }

    public CoolString type_name() {
        final String simpleName = this.getClass().getSimpleName();
        return new CoolString(simpleName.substring(4, simpleName.length() - 1));
    }

    @Override
    public boolean equals(Object other) {
        return this == other;
    }
}
