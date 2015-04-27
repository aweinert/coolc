package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class IDivInstruction extends OpCode {

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeIDiv();
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getIDivLength();
    }

}
