package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

public class MethodRef extends ConstantPoolEntry {

    private final char classRefIndex;
    private final char nameAndTypeIndex;

    public MethodRef(char classRefIndex, char nameAndTypeIndex) {
        super((byte) 0x0a);
        this.classRefIndex = classRefIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    @Override
    public void encode(JbcEncoder jbcEncoder) {
        jbcEncoder.encodeMethodRef(this.classRefIndex, this.nameAndTypeIndex);
    }

}
