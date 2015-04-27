package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class ISubInstruction extends OpCode {

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeISub();
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getISubLength();
    }

}
