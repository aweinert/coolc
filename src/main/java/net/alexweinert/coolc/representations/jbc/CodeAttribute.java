package net.alexweinert.coolc.representations.jbc;

import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;
import net.alexweinert.coolc.representations.jbc.instructions.OpCode;

public class CodeAttribute extends AttributeEntry {

    public static class Builder {
        private final char attributeNameIndex;
        private final char maxStack;
        private final char maxLocals;
        private List<OpCode> code = new LinkedList<>();
        private final List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        private final List<AttributeEntry> attributes = new LinkedList<>();

        public Builder(char attributeNameIndex, char maxStack, char maxLocals) {
            this.attributeNameIndex = attributeNameIndex;
            this.maxStack = maxStack;
            this.maxLocals = maxLocals;
        }

        public Builder addOpCode(OpCode code) {
            this.code.add(code);
            return this;
        }

        public Builder addException(ExceptionTableEntry exception) {
            this.exceptionTable.add(exception);
            return this;
        }

        public Builder addAttribute(AttributeEntry attribute) {
            this.attributes.add(attribute);
            return this;
        }

        public CodeAttribute build(JbcEncoding encoding) {
            final int attributeLength = this.computeAttributeLength(encoding);
            final int codeLength = this.computeCodeLength(encoding);
            assert this.exceptionTable.size() < Character.MAX_VALUE;
            final char exceptionTableLength = (char) this.exceptionTable.size();
            assert this.attributes.size() < Character.MAX_VALUE;
            final char attributesCount = (char) this.attributes.size();
            return new CodeAttribute(this.attributeNameIndex, attributeLength, this.maxStack, this.maxLocals,
                    codeLength, code, exceptionTableLength, exceptionTable, attributesCount, attributes);
        }

        private int computeAttributeLength(JbcEncoding encoding) {
            int attributeLength = 0;
            // Length of maxStack
            attributeLength += encoding.getCharLength();
            // Length of maxLocals
            attributeLength += encoding.getCharLength();
            // Length of codeLength
            attributeLength += encoding.getIntLength();
            // Length of code
            for (OpCode opCode : code) {
                attributeLength += encoding.getOpcodeLength(opCode);
            }
            // Length of exceptionTableLength
            attributeLength += encoding.getCharLength();
            // Length of exceptionTable
            for (ExceptionTableEntry exceptionTableEntry : this.exceptionTable) {
                attributeLength += encoding.getExceptionTableEntryLength(exceptionTableEntry);
            }
            // Length of attributesCount
            attributeLength += encoding.getCharLength();
            // Length of attributes
            for (AttributeEntry attribute : this.attributes) {
                attributeLength += encoding.getAttributeEntryLength(attribute);
            }
            return attributeLength;
        }

        private int computeCodeLength(JbcEncoding encoding) {
            int codeLength = 0;
            for (OpCode opCode : code) {
                codeLength += encoding.getOpcodeLength(opCode);
            }
            return codeLength;
        }

        public void setCode(List<OpCode> code) {
            this.code = code;
        }

    }

    private final char attributeNameIndex;
    private final int attributeLength;
    private final char maxStack;
    private final char maxLocals;
    private final int codeLength;
    private final List<OpCode> code;
    private final char exceptionTableLength;
    private final List<ExceptionTableEntry> exceptionTable;
    private final char attributesCount;
    private final List<AttributeEntry> attributes;

    private CodeAttribute(char attributeNameIndex, int attributeLength, char maxStack, char maxLocals, int codeLength,
            List<OpCode> code, char exceptionTableLength, List<ExceptionTableEntry> exceptionTable,
            char attributesCount, List<AttributeEntry> attributes) {
        this.attributeNameIndex = attributeNameIndex;
        this.attributeLength = attributeLength;
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
        this.codeLength = codeLength;
        this.code = code;
        this.exceptionTableLength = exceptionTableLength;
        this.exceptionTable = exceptionTable;
        this.attributesCount = attributesCount;
        this.attributes = attributes;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        encoder.encodeCodeAttribute(this);

    }

    public char getAttributeNameIndex() {
        return attributeNameIndex;
    }

    public int getAttributeLength() {
        return attributeLength;
    }

    public char getMaxStack() {
        return maxStack;
    }

    public char getMaxLocals() {
        return maxLocals;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public List<OpCode> getCode() {
        return code;
    }

    public char getExceptionTableLength() {
        return exceptionTableLength;
    }

    public List<ExceptionTableEntry> getExceptionTable() {
        return exceptionTable;
    }

    public char getAttributesCount() {
        return attributesCount;
    }

    public List<AttributeEntry> getAttributes() {
        return attributes;
    }

}
