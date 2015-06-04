package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class AStoreInstruction extends OpCode {

    private final char varId;

    AStoreInstruction(char varId) {
        this.varId = varId;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        assert this.varId < Character.MAX_VALUE;
        encoder.encodeAStore((char) this.varId);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getAStoreLength();
    }

}
