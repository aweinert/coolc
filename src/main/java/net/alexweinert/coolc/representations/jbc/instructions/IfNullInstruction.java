package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class IfNullInstruction extends OpCode {

    private final char target;

    IfNullInstruction(char target) {
        this.target = target;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeIfNull(this.target);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getIfNullLength();
    }

}
