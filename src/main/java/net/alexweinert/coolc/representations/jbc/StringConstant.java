package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

public class StringConstant extends ConstantPoolEntry {

    private final char utf8Index;

    StringConstant(char utf8Index) {
        super((byte) 0x08);
        this.utf8Index = utf8Index;
    }

    @Override
    public void encode(JbcEncoder jbcEncoder) {
        jbcEncoder.encodeStringConstant(this.utf8Index);
    }
}
