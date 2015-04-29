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

    public int getL2ILength() {
        return 1;
    }

    public int getI2kLength() {
        return 1;
    }

    public int getALoadLength() {
        return 2;
    }

    public int getALoad0Length() {
        return 1;
    }

    public int getALoad1Length() {
        return 1;
    }

    public int getALoad2Length() {
        return 1;
    }

    public int getALoad3Length() {
        return 1;
    }

    public int getInvokeVirtualLength() {
        return 3;
    }

    public int getIAddLength() {
        return 1;
    }

    public int getIDivLength() {
        return 1;
    }

    public int getIMulLength() {
        return 1;
    }

    public int getISubLength() {
        return 1;
    }

    public int getAConstNullLength() {
        return 1;
    }

    public int getAReturnLength() {
        return 1;
    }

    public int getAStoreLength() {
        return 2;
    }

    public int getIConst0Length() {
        return 1;
    }

    public int getIConst1Length() {
        return 1;
    }

    public int getIfEqLength() {
        return 3;
    }

    public int getIfIcmpLeLength() {
        return 3;
    }

    public int getIfIcmpLtLength() {
        return 3;
    }

    public int getIfNeqLength() {
        return 3;
    }

    public int getIfNullLength() {
        return 3;
    }

    public int getInstanceOfLength() {
        return 3;
    }

    public int getInvokeDynamicLength() {
        return 5;
    }

    public int getLdcLength() {
        return 2;
    }

    public int getNewLength() {
        return 3;
    }

    public int getSiPushLength() {
        return 3;
    }

    public int getReturnLength() {
        return 1;
    }

    public int getDupLength() {
        return 1;
    }

    public int getInvokeSpecialLength() {
        return 3;
    }

    public int getPopLength() {
        return 1;
    }

}
