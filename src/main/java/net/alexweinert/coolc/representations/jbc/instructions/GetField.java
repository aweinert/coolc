package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class GetField extends OpCode {

    private char fieldRefId;

    public GetField(char fieldRefId) {
        this.fieldRefId = fieldRefId;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeGetField(this.fieldRefId);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getGetFieldLength();
    }

}
