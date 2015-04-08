package net.alexweinert.coolc.representations.jbc;

public abstract class ConstantPoolEntry {
    public static class Builder {
        public ClassConstant buildClassConstant(char idIndex) {
            return new ClassConstant(idIndex);
        }

        public Utf8Constant buildUtf8Constant(String value) {
            return new Utf8Constant(value);
        }
    }

    protected final byte tag;

    protected ConstantPoolEntry(byte tag) {
        this.tag = tag;
    }

    public abstract byte[] toBytes();
}
