package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class IConst1Instruction extends OpCode {

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeIConst1();
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getIConst1Length();
    }

}
