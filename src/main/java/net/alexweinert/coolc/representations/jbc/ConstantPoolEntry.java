package net.alexweinert.coolc.representations.jbc;

public abstract class ConstantPoolEntry {
    protected final byte tag;

    protected ConstantPoolEntry(byte tag) {
        this.tag = tag;
    }

    public abstract byte[] toBytes();
}
