package net.alexweinert.coolc.representations.jbc;

public class ClassConstant extends ConstantPoolEntry {
    private final char nameIndex;

    ClassConstant(char nameIndex) {
        super((byte) 0x07);
        this.nameIndex = nameIndex;
    }

    public char getNameIndex() {
        return this.nameIndex;
    }

    @Override
    public byte[] toBytes() {
        return new byte[] { this.tag, (byte) ((this.nameIndex + 1) >>> 8 & 0xFF), (byte) ((this.nameIndex + 1) & 0xFF) };
    }
}
