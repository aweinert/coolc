package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class IMulInstruction extends OpCode {

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeIMul();
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getIMulLength();
    }

}