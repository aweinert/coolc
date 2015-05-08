package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class LdcWInstruction extends OpCode {

    private final char constRefId;

    LdcWInstruction(char constRefId) {
        this.constRefId = constRefId;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeLdcW((char) this.constRefId);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getLdcWLength();
    }

}
