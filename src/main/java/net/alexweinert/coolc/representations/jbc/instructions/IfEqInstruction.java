package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

public class IfEqInstruction extends OpCode {

    private final char target;

    IfEqInstruction(char target) {
        this.target = target;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeIfEq(target);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getIfEqLength();
    }

}
