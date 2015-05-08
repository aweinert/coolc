package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

public class ClassConstant extends ConstantPoolEntry {
    private final char nameIndex;

    ClassConstant(char nameIndex) {
        super((byte) 0x07);
        this.nameIndex = nameIndex;
    }

    public char getNameIndex() {
        return this.nameIndex;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeClassConstant(this.tag, this.nameIndex);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + nameIndex;
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
        ClassConstant other = (ClassConstant) obj;
        if (nameIndex != other.nameIndex) {
            return false;
        }
        return true;
    }
}
