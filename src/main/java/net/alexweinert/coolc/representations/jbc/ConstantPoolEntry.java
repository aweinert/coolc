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

        public ConstantPoolEntry buildFieldRef(char classRefId, char nameAndTypeRefId) {
            return new FieldRefConstant(classRefId, nameAndTypeRefId);
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tag;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ConstantPoolEntry other = (ConstantPoolEntry) obj;
        if (tag != other.tag) {
            return false;
        }
        return true;
    }
}
