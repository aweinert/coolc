package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class NewInstruction extends OpCode {
    private final char classRefId;

    NewInstruction(char classRefId) {
        this.classRefId = classRefId;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeNew(this.classRefId);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getNewLength();
    }

}
