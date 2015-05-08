package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

public class MethodRef extends ConstantPoolEntry {

    private final char classRefIndex;
    private final char nameAndTypeIndex;

    public MethodRef(char classRefIndex, char nameAndTypeIndex) {
        super((byte) 0x0a);
        this.classRefIndex = classRefIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    @Override
    public void encode(JbcEncoder jbcEncoder) {
        jbcEncoder.encodeMethodRef(this.classRefIndex, this.nameAndTypeIndex);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + classRefIndex;
        result = prime * result + nameAndTypeIndex;
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
        MethodRef other = (MethodRef) obj;
        if (classRefIndex != other.classRefIndex) {
            return false;
        }
        if (nameAndTypeIndex != other.nameAndTypeIndex) {
            return false;
        }
        return true;
    }

}
