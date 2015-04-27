package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class IfIcmpLtInstruction extends OpCode {

    private final char target;

    IfIcmpLtInstruction(char target) {
        this.target = target;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeIfIcmpLt(target);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getIfIcmpLtLength();
    }

}
