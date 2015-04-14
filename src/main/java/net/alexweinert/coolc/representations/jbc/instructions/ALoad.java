package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class ALoad extends OpCode {

    private final byte index;

    ALoad(byte index) {
        this.index = index;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        if (this.index == 0) {
            encoder.encodeALoad0();
        } else if (this.index == 1) {
            encoder.encodeALoad1();
        } else if (this.index == 2) {
            encoder.encodeALoad2();
        } else if (this.index == 3) {
            encoder.encodeALoad3();
        } else {
            encoder.encodeALoad(this.index);
        }
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        if (this.index == 0) {
            return encoding.getALoad0Length();
        } else if (this.index == 1) {
            return encoding.getALoad1Length();
        } else if (this.index == 2) {
            return encoding.getALoad2Length();
        } else if (this.index == 3) {
            return encoding.getALoad3Length();
        } else {
            return encoding.getALoadLength();
        }
    }

}
