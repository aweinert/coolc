package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class InvokeSpecial extends OpCode {

    private final char methodRefId;

    public InvokeSpecial(char methodRefId) {
        this.methodRefId = methodRefId;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeInvokeSpecial(this.methodRefId);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getInvokeSpecialLength();
    }

}
