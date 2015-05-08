package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

public class Utf8Constant extends ConstantPoolEntry {
    private final String value;

    Utf8Constant(String value) {
        super((byte) 0x01);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void encode(JbcEncoder jbcEncoder) {
        jbcEncoder.encodeUtf8Constant(this.tag, this.value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        Utf8Constant other = (Utf8Constant) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

}
