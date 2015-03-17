public class CoolObject {
    public CoolObject coolabort() {
        System.exit(1);
        return null;
    }

    public CoolString cooltype_name() {
        final String simpleName = this.getClass().getSimpleName();
        return new CoolString(simpleName.substring(4, simpleName.length() - 1));
    }
}
