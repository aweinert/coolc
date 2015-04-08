package net.alexweinert.coolc.representations.jbc;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FieldEntry {

    public static class Builder {
        private char nameIndex;
        private char descriptorIndex;
        private List<AttributeEntry> attributes = new LinkedList<>();

        public Builder(char nameIndex, char descriptorIndex) {
            this.nameIndex = nameIndex;
            this.descriptorIndex = descriptorIndex;
        }

        public Builder addAttribute(AttributeEntry attribute) {
            this.attributes.add(attribute);
            return this;
        }

        public FieldEntry build() {
            return new FieldEntry(nameIndex, descriptorIndex, attributes);
        }
    }

    private char nameIndex;
    private char descriptorIndex;
    private List<AttributeEntry> attributes;

    private FieldEntry(char nameIndex, char descriptorIndex, List<AttributeEntry> attributes) {
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributes = attributes;
    }

    public byte[] toBytes() {
        byte[] returnValue = new byte[] { 0x00, 0x04, (byte) (nameIndex + 1 >>> 8 & 0xFF),
                (byte) (nameIndex + 1 & 0xFF), (byte) (descriptorIndex + 1 >>> 8 & 0xFF),
                (byte) (descriptorIndex + 1 & 0xFF) };
        assert this.attributes.size() < Character.MAX_VALUE : "Too many attributes for Field";
        returnValue = Arrays.copyOf(returnValue, returnValue.length + 2);
        returnValue[returnValue.length - 2] = (byte) (this.attributes.size() >>> 8 & 0xFF);
        returnValue[returnValue.length - 1] = (byte) (this.attributes.size() >>> 0xFF);
        for (AttributeEntry attribute : this.attributes) {
            final byte[] attributeBytes = attribute.toBytes();
            returnValue = Arrays.copyOf(returnValue, returnValue.length + attributeBytes.length);
            System.arraycopy(attributeBytes, 0, returnValue, returnValue.length - attributeBytes.length,
                    attributeBytes.length);
        }
        return returnValue;
    }
}
