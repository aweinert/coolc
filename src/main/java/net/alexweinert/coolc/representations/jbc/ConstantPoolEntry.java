package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

public abstract class ConstantPoolEntry {
    public static class Builder {
        public ClassConstant buildClassConstant(char idIndex) {
            return new ClassConstant(idIndex);
        }

        public Utf8Constant buildUtf8Constant(String value) {
            return new Utf8Constant(value);
        }

        public ConstantPoolEntry buildNameAndType(char getValueMethodNameStringId, char getValueMethodTypeStringId) {
            // TODO Auto-generated method stub
            return null;
        }

        public ConstantPoolEntry buildMethodRef(char coolIntClassRefIndex, char getValueMethodNameAndTypeId) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    protected final byte tag;

    protected ConstantPoolEntry(byte tag) {
        this.tag = tag;
    }

    public abstract void encode(JbcEncoder jbcEncoder);
}
