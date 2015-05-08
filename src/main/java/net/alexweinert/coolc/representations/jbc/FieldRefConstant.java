package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

class FieldRefConstant extends ConstantPoolEntry {

    private final char classRefId;
    private final char nameAndTypeRefId;

    public FieldRefConstant(char classRefId, char nameAndTypeRefId) {
        super((byte) 0x09);
        this.classRefId = classRefId;
        this.nameAndTypeRefId = nameAndTypeRefId;
    }

    @Override
    public void encode(JbcEncoder jbcEncoder) {
        jbcEncoder.encodeFieldRefConstant(this.classRefId, this.nameAndTypeRefId);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + classRefId;
        result = prime * result + nameAndTypeRefId;
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
        FieldRefConstant other = (FieldRefConstant) obj;
        if (classRefId != other.classRefId) {
            return false;
        }
        if (nameAndTypeRefId != other.nameAndTypeRefId) {
            return false;
        }
        return true;
    }

}
