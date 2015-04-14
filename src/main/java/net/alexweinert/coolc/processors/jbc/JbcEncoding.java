package net.alexweinert.coolc.processors.jbc;

import net.alexweinert.coolc.representations.jbc.AttributeEntry;
import net.alexweinert.coolc.representations.jbc.ExceptionTableEntry;
import net.alexweinert.coolc.representations.jbc.instructions.OpCode;

public class JbcEncoding {

    public static JbcEncoding createStandardEncoding() {
        return new JbcEncoding();
    }

    public int getCharLength() {
        return 2;
    }

    public int getIntLength() {
        return 4;
    }

    public int getOpcodeLength(OpCode opCode) {
        return opCode.getLength(this);
    }

    public int getNopLength() {
        return 1;
    }

    public int getExceptionTableEntryLength(ExceptionTableEntry exceptionTableEntry) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getAttributeEntryLength(AttributeEntry attribute) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getGotoLength() {
        return 3;
    }

}
