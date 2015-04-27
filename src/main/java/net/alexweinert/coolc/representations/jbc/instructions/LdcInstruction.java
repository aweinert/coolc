package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class LdcInstruction extends OpCode {

    private final char constRefId;

    LdcInstruction(char constRefId) {
        this.constRefId = constRefId;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        assert this.constRefId < Byte.MAX_VALUE;
        encoder.encodeLdc((byte) this.constRefId);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getLdcLength();
    }

}
