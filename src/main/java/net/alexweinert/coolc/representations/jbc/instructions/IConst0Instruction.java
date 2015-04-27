package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class IConst0Instruction extends OpCode {

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeIConst0();
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getIConst0Length();
    }

}
