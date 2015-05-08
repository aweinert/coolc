package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

public class StringConstant extends ConstantPoolEntry {

    private final char utf8Index;

    StringConstant(char utf8Index) {
        super((byte) 0x08);
        this.utf8Index = utf8Index;
    }

    @Override
    public void encode(JbcEncoder jbcEncoder) {
        jbcEncoder.encodeStringConstant(this.utf8Index);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + utf8Index;
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
        StringConstant other = (StringConstant) obj;
        if (utf8Index != other.utf8Index) {
            return false;
        }
        return true;
    }
}
