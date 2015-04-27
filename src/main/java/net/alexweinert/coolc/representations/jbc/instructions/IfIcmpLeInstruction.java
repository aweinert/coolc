package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class IfIcmpLeInstruction extends OpCode {

    private final char target;

    IfIcmpLeInstruction(char target) {
        this.target = target;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeIfIcmpLe(target);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getIfIcmpLeLength();
    }

}
