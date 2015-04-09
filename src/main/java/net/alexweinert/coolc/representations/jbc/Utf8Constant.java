package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

public class Utf8Constant extends ConstantPoolEntry {
    private final String value;

    Utf8Constant(String value) {
        super((byte) 0x01);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void encode(JbcEncoder jbcEncoder) {
        jbcEncoder.encodeUtf8Constant(this.tag, this.value);
    }

}
