package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class InstanceOfInstruction extends OpCode {

    private final char classRefId;

    InstanceOfInstruction(char classRefId) {
        this.classRefId = classRefId;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeInstanceOf(classRefId);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getInstanceOfLength();
    }

}
