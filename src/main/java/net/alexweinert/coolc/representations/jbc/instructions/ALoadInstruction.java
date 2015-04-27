package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class ALoadInstruction extends OpCode {

    private final char varIndex;

    ALoadInstruction(char varIndex) {
        this.varIndex = varIndex;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        assert this.varIndex < Byte.MAX_VALUE;
        encoder.encodeALoad((byte) this.varIndex);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getALoadLength();
    }

}
