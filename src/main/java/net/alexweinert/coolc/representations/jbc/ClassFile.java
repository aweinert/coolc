package net.alexweinert.coolc.representations.jbc;

import java.util.LinkedList;
import java.util.List;

public class ClassFile {
    public static class Builder {
        private final String id;
        private String parent = null;

        public Builder(String id) {
            this.id = id;
        }

        public Builder setParent(String parent) {
            this.parent = parent;
            return this;
        }

        public ClassFile build() {
            final List<Byte> constants = new LinkedList<>();
            constants.add(Byte.valueOf((byte) 0x07)); // Class-tag
            constants.add(Byte.valueOf((byte) 0x00));
            constants.add(Byte.valueOf((byte) 0x02));

            constants.add(Byte.valueOf((byte) 0x01)); // UTF8-tag
            constants.add(Byte.valueOf((byte) 0x00)); // String length (high)
            constants.add(Byte.valueOf((byte) 0x03)); // String length (low)
            for (byte character : this.id.getBytes()) {
                constants.add(character);
            }

            constants.add(Byte.valueOf((byte) 0x07)); // Class-tag
            constants.add(Byte.valueOf((byte) 0x00));
            constants.add(Byte.valueOf((byte) 0x04));

            constants.add(Byte.valueOf((byte) 0x01)); // UTF8-tag
            constants.add(Byte.valueOf((byte) 0x00)); // String length (high)
            constants.add(Byte.valueOf((byte) 0x06)); // String length (low)
            for (byte character : this.parent.getBytes()) {
                constants.add(character);
            }

            final byte[] constantsArray = new byte[constants.size()];
            for (int i = 0; i < constants.size(); ++i) {
                constantsArray[i] = constants.get(i);
            }
            return new ClassFile(constantsArray, (char) 0x01, (char) 0x03);
        }
    }

    private final byte[] constants;

    private final char thisClassIndex;
    private final char superClassIndex;

    private ClassFile(byte[] constants, char thisClassIndex, char superClassIndex) {
        this.constants = constants;
        this.thisClassIndex = thisClassIndex;
        this.superClassIndex = superClassIndex;
    }

    public byte[] getMinorVersion() {
        return new byte[] { 0x00, 0x00 };
    }

    public byte[] getMajorVersion() {
        return new byte[] { 0x00, 0x33 };
    }

    public String getClassId() {
        return "blub";
    }

    public byte[] getConstantsCount() {
        return new byte[] { 0x00, 0x05 };
    }

    public byte[] getConstants() {
        return this.constants;
    }

    public byte[] getAccessFlags() {
        return new byte[] { 0x00, 0x00 };
    }

    public char getThisClassIndex() {
        return thisClassIndex;
    }

    public char getSuperClassIndex() {
        return superClassIndex;
    }

    public byte[] getInterfacesCount() {
        return new byte[] { 0x00, 0x00 };
    }

    public byte[] getInterfaces() {
        return new byte[0];
    }

    public byte[] getFieldsCount() {
        return new byte[] { 0x00 };
    }

    public byte[] getFields() {
        return new byte[0];
    }

    public byte[] getMethodsCount() {
        return new byte[] { 0x00 };
    }

    public byte[] getMethods() {
        return new byte[0];
    }

    public byte[] getAttributesCount() {
        return new byte[] { 0x00 };
    }

    public byte[] getAttributes() {
        return new byte[0];
    }

}
