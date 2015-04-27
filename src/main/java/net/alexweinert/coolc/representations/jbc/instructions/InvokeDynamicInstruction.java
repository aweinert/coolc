package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class InvokeDynamicInstruction extends OpCode {

    private final char methodRefId;

    InvokeDynamicInstruction(char methodRefId) {
        this.methodRefId = methodRefId;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeInvokeDynamic(this.methodRefId);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getInvokeDynamicLength();
    }

}
