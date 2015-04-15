package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

public class InvokeVirtual extends OpCode {

    private final char index;

    InvokeVirtual(char index) {
        this.index = index;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeInvokeVirtual(this.index);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getInvokeVirtualLength();
    }

}
