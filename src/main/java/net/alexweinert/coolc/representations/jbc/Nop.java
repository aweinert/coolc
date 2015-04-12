package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

public class Nop extends OpCode {

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeNop();

    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getNopLength();
    }

}
