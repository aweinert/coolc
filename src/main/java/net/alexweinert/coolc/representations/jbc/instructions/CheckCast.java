package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class CheckCast extends OpCode {

    private final char classRefId;

    public CheckCast(char classRefId) {
        this.classRefId = classRefId;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeCheckCast(this.classRefId);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getCheckCastLength();
    }

}
