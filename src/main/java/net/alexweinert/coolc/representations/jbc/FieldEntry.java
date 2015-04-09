package net.alexweinert.coolc.representations.jbc;

import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

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

    public void encode(JbcEncoder jbcEncoder) {
        jbcEncoder.encodeField(this.nameIndex, this.descriptorIndex, this.attributes);
    }
}
