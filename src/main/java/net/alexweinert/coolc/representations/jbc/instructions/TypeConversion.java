package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class TypeConversion extends OpCode {
    enum Kind {
        L2I, I2L
    }

    final private Kind kind;

    TypeConversion(Kind kind) {
        this.kind = kind;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        if (this.kind == Kind.L2I) {
            encoder.encodeL2I();
        } else if (this.kind == Kind.I2L) {
            encoder.encodeI2L();
        }
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        if (this.kind == Kind.L2I) {
            return encoding.getL2ILength();
        } else if (this.kind == Kind.I2L) {
            return encoding.getI2kLength();
        } else {
            assert false;
            return -1;
        }
    }

}
