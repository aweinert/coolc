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

        public ConstantPoolEntry buildNameAndType(char nameId, char typeId) {
            return new NameAndType(nameId, typeId);
        }

        public ConstantPoolEntry buildMethodRef(char classRefIndex, char nameAndTypeIndex) {
            return new MethodRef(classRefIndex, nameAndTypeIndex);
        }

        public ConstantPoolEntry buildStringConstant(char utf8Index) {
            return new StringConstant(utf8Index);
        }
    }

    protected final byte tag;

    protected ConstantPoolEntry(byte tag) {
        this.tag = tag;
    }

    public abstract void encode(JbcEncoder jbcEncoder);
}
