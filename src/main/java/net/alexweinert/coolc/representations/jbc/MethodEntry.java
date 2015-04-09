package net.alexweinert.coolc.representations.jbc;

import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

public class MethodEntry {

    public static class Builder {
        private final char nameIndex;
        private final char descriptorIndex;
        private final List<AttributeEntry> attributes = new LinkedList<>();

        public Builder(char nameIndex, char descriptorIndex) {
            this.nameIndex = nameIndex;
            this.descriptorIndex = descriptorIndex;
        }

        public Builder addAttribute(final AttributeEntry attribute) {
            this.attributes.add(attribute);
            return this;
        }

        public MethodEntry build() {
            return new MethodEntry(nameIndex, descriptorIndex, attributes);
        }
    }

    private final char nameIndex;
    private final char descriptorIndex;
    private final List<AttributeEntry> attributes;

    private MethodEntry(char nameIndex, char descriptorIndex, List<AttributeEntry> attributes) {
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributes = attributes;
    }

    public void encode(JbcEncoder jbcEncoder) {
        jbcEncoder.encodeMethod(this.nameIndex, this.descriptorIndex, this.attributes);
    }
}
