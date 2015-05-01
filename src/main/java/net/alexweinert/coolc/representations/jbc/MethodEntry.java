package net.alexweinert.coolc.representations.jbc;

import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

public class MethodEntry {

    public static class Builder {
        private final char nameIndex;
        private final char descriptorIndex;
        private final List<AttributeEntry> attributes = new LinkedList<>();

        private boolean isStatic = false;

        public Builder(char nameIndex, char descriptorIndex) {
            this.nameIndex = nameIndex;
            this.descriptorIndex = descriptorIndex;
        }

        public Builder addAttribute(final AttributeEntry attribute) {
            this.attributes.add(attribute);
            return this;
        }

        public MethodEntry build() {
            return new MethodEntry(nameIndex, descriptorIndex, isStatic, attributes);
        }

        public void setStatic(boolean isStatic) {
            this.isStatic = isStatic;
        }
    }

    private final char nameIndex;
    private final char descriptorIndex;
    private final boolean isStatic;
    private final List<AttributeEntry> attributes;

    private MethodEntry(char nameIndex, char descriptorIndex, boolean isStatic, List<AttributeEntry> attributes) {
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.isStatic = isStatic;
        this.attributes = attributes;
    }

    public void encode(JbcEncoder jbcEncoder) {
        if (this.isStatic) {
            jbcEncoder.encodeStaticMethod(this.nameIndex, this.descriptorIndex, this.attributes);
        } else {
            jbcEncoder.encodeMethod(this.nameIndex, this.descriptorIndex, this.attributes);
        }
    }
}
