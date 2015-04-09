package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

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
    public void encode(JbcEncoder encoder) {
        encoder.encodeClassConstant(this.tag, this.nameIndex);
    }
}
