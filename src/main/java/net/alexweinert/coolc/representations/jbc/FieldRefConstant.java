package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

class FieldRefConstant extends ConstantPoolEntry {

    private final char classRefId;
    private final char nameAndTypeRefId;

    public FieldRefConstant(char classRefId, char nameAndTypeRefId) {
        super((byte) 0x09);
        this.classRefId = classRefId;
        this.nameAndTypeRefId = nameAndTypeRefId;
    }

    @Override
    public void encode(JbcEncoder jbcEncoder) {
        jbcEncoder.encodeFieldRefConstant(this.classRefId, this.nameAndTypeRefId);

    }

}
