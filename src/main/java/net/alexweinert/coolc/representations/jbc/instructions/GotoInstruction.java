package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

public class GotoInstruction extends OpCode {

    private char target;

    GotoInstruction(char target) {
        this.target = target;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeGoto(this.target);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getGotoLength();
    }

}
