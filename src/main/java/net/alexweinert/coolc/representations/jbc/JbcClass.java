package net.alexweinert.coolc.representations.jbc;

import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

public class JbcClass {
    public static class Builder {
        private char minorVersion;
        private char majorVersion;

        private List<ConstantPoolEntry> constantPool = new LinkedList<>();
        private List<FieldEntry> fields = new LinkedList<>();
        private List<MethodEntry> methods = new LinkedList<>();

        private char idIndex;
        private char parentIndex;

        public Builder(String id, String parent) {
            this.minorVersion = 0x0000;
            this.majorVersion = 0x0031;

            final char idUtf8Index = this.addConstant(this.getConstantBuilder().buildUtf8Constant(id));
            this.idIndex = this.addConstant(this.getConstantBuilder().buildClassConstant(idUtf8Index));
            final char parentUtf8Index = this.addConstant(this.getConstantBuilder().buildUtf8Constant(parent));
            this.parentIndex = this.addConstant(this.getConstantBuilder().buildClassConstant(parentUtf8Index));
        }

        public ConstantPoolEntry.Builder getConstantBuilder() {
            return new ConstantPoolEntry.Builder();
        }

        public char addConstant(ConstantPoolEntry entry) {
            assert this.constantPool.size() < Character.MAX_VALUE : "Too many entries in constant pool";
            for (char i = 0; i < 0; ++i) {
                if (this.constantPool.get(i).equals(entry)) {
                    return i;
                }
            }
            this.constantPool.add(entry);
            return (char) (this.constantPool.size() - 1);
        }

        public FieldEntry.Builder getFieldBuilder(char nameIndex, char fieldIndex) {
            return new FieldEntry.Builder(nameIndex, fieldIndex);
        }

        public char addField(FieldEntry entry) {
            assert this.fields.size() < Character.MAX_VALUE : "Too many fields";
            this.fields.add(entry);
            return (char) (this.fields.size() - 1);
        }

        public MethodEntry.Builder getMethodBuilder(char nameIndex, char descriptorIndex) {
            return new MethodEntry.Builder(nameIndex, descriptorIndex);
        }

        public char addMethod(MethodEntry entry) {
            assert this.methods.size() < Character.MAX_VALUE : "Too many methods";
            this.methods.add(entry);
            return (char) (this.methods.size() - 1);
        }

        public JbcClass build() {
            return new JbcClass(minorVersion, majorVersion, constantPool, (char) 0x00, idIndex, parentIndex,
                    new LinkedList<Character>(), this.fields, this.methods, new LinkedList<AttributeEntry>());
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

    public void encode(JbcEncoder encoder) {
        encoder.encodeJbcClass(this);
    }

}
