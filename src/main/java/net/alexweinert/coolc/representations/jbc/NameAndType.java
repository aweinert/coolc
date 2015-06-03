package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

public class NameAndType extends ConstantPoolEntry {

    final private char nameId;
    final private char typeId;

    public NameAndType(char nameId, char typeId) {
        super((byte) 0x0c);
        this.nameId = nameId;
        this.typeId = typeId;
    }

    @Override
    public void encode(JbcEncoder jbcEncoder) {
        jbcEncoder.encodeNameAndType(this.nameId, this.typeId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + nameId;
        result = prime * result + typeId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NameAndType other = (NameAndType) obj;
        if (nameId != other.nameId) {
            return false;
        }
        if (typeId != other.typeId) {
            return false;
        }
        return true;
    }
}
