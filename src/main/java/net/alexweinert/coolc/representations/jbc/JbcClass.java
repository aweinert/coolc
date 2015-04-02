package net.alexweinert.coolc.representations.jbc;

import java.util.LinkedList;
import java.util.List;

public class JbcClass {
    public static class Builder {
        private char minorVersion;
        private char majorVersion;

        private List<ConstantPoolEntry> constantPool = new LinkedList<>();

        private char idIndex;
        private char parentIndex;

        public Builder(String id, String parent) {
            this.minorVersion = 0x0000;
            this.majorVersion = 0x0033;

            this.idIndex = this.addClassConstant(this.addStringConstant(id));
            this.parentIndex = this.addClassConstant(this.addStringConstant(parent));
        }

        public char addStringConstant(String value) {
            assert this.constantPool.size() < Character.MAX_VALUE : "Too many entries in constant pool";
            this.constantPool.add(new Utf8Constant(value));
            return (char) (this.constantPool.size() - 1);
        }

        public char addClassConstant(char nameIndex) {
            assert this.constantPool.size() < Character.MAX_VALUE : "Too many entries in constant pool";
            this.constantPool.add(new ClassConstant(nameIndex));
            return (char) (this.constantPool.size() - 1);
        }

        public JbcClass build() {
            return new JbcClass(minorVersion, majorVersion, constantPool, (char) 0x00, idIndex, parentIndex,
                    new LinkedList<Character>(), new LinkedList<FieldEntry>(), new LinkedList<MethodEntry>(),
                    new LinkedList<AttributeEntry>());
        }
    }

    final private char minorVersion;
    final private char majorVersion;

    final private List<ConstantPoolEntry> constantPool;

    final private char accessFlags;
    final private char idIndex;
    final private char parentIndex;

    final private List<Character> interfaces;
    final private List<FieldEntry> fields;
    final private List<MethodEntry> methods;
    final private List<AttributeEntry> attributes;

    private JbcClass(char minorVersion, char majorVersion, List<ConstantPoolEntry> constantPool, char accessFlags,
            char idIndex, char parentIndex, List<Character> interfaces, List<FieldEntry> fields,
            List<MethodEntry> methods, List<AttributeEntry> attributes) {
        this.minorVersion = minorVersion;
        this.majorVersion = majorVersion;
        this.constantPool = constantPool;
        this.accessFlags = accessFlags;
        this.idIndex = idIndex;
        this.parentIndex = parentIndex;
        this.interfaces = interfaces;
        this.fields = fields;
        this.methods = methods;
        this.attributes = attributes;
    }

    public char getMinorVersion() {
        return minorVersion;
    }

    public char getMajorVersion() {
        return majorVersion;
    }

    public List<ConstantPoolEntry> getConstantPool() {
        return constantPool;
    }

    public char getAccessFlags() {
        return accessFlags;
    }

    public char getIdIndex() {
        return idIndex;
    }

    public char getParentIndex() {
        return parentIndex;
    }

    public List<Character> getInterfaces() {
        return interfaces;
    }

    public List<FieldEntry> getFields() {
        return fields;
    }

    public List<MethodEntry> getMethods() {
        return methods;
    }

    public List<AttributeEntry> getAttributes() {
        return attributes;
    }

}
