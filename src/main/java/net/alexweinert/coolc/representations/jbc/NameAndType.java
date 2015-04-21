package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

public class NameAndType extends ConstantPoolEntry {

    final private char nameId;
    final private char typeId;

    public NameAndType(char nameId, char typeId) {
        super((byte) 0x0c);
        this.nameId = nameId;
        this.typeId = typeId;
    }

    @Override
    public void encode(JbcEncoder jbcEncoder) {
        jbcEncoder.encodeNameAndType(this.nameId, this.typeId);
    }
}
