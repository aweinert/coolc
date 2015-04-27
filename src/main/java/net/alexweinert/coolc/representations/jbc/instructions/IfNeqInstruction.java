package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class IfNeqInstruction extends OpCode {

    private final char target;

    IfNeqInstruction(char target) {
        this.target = target;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeIfNeq(this.target);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getIfNeqLength();
    }

}
