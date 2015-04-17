package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

class IntegerArithmetic extends OpCode {

    private enum Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

    private final Operation opType;

    IntegerArithmetic(Operation opType) {
        this.opType = opType;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        switch (this.opType) {
        case ADD:
            encoder.encodeIAdd();
            break;
        case DIVIDE:
            encoder.encodeIDiv();
            break;
        case MULTIPLY:
            encoder.encodeIMul();
            break;
        case SUBTRACT:
            encoder.encodeISub();
            break;
        default:
            assert false;
        }
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        switch (this.opType) {
        case ADD:
            return encoding.getIAddLength();
        case DIVIDE:
            return encoding.getIDivLength();
        case MULTIPLY:
            return encoding.getIMulLength();
        case SUBTRACT:
            return encoding.getISubLength();
        default:
            assert false;
            return -1;
        }
    }

}
