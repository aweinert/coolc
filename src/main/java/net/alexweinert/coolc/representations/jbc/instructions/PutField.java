package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class PutField extends OpCode {

    private char fieldRefId;

    public PutField(char fieldRefId) {
        this.fieldRefId = fieldRefId;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodePutField(this.fieldRefId);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getPutFieldLength();
    }

}
