package net.alexweinert.coolc.processors.jbc;

import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import net.alexweinert.coolc.representations.io.File;
import net.alexweinert.coolc.representations.io.File.Builder;
import net.alexweinert.coolc.representations.jbc.AttributeEntry;
import net.alexweinert.coolc.representations.jbc.ClassConstant;
import net.alexweinert.coolc.representations.jbc.CodeAttribute;
import net.alexweinert.coolc.representations.jbc.ConstantPoolEntry;
import net.alexweinert.coolc.representations.jbc.ExceptionTableEntry;
import net.alexweinert.coolc.representations.jbc.FieldEntry;
import net.alexweinert.coolc.representations.jbc.JbcClass;
import net.alexweinert.coolc.representations.jbc.MethodEntry;
import net.alexweinert.coolc.representations.jbc.Utf8Constant;
import net.alexweinert.coolc.representations.jbc.instructions.OpCode;

public class JbcEncoder {
    public static File encode(JbcClass jbcClass) {
        final ClassConstant thisConstant = (ClassConstant) jbcClass.getConstantPool().get(jbcClass.getIdIndex());
        final Utf8Constant thisId = (Utf8Constant) jbcClass.getConstantPool().get(thisConstant.getNameIndex());
        final JbcEncoder encoder = new JbcEncoder(new File.Builder(Paths.get(thisId.getValue() + ".class")));
        jbcClass.encode(encoder);
        return encoder.builder.build();
    }

    private final File.Builder builder;
    private final ByteSplitter splitter = new ByteSplitter();

    private JbcEncoder(Builder builder) {
        this.builder = builder;
    }

    public void encodeJbcClass(JbcClass jbcClass) {
        builder.appendContent(splitter.splitInt(0xCAFEBABE));

        builder.appendContent(splitter.splitChar(jbcClass.getMinorVersion()));
        builder.appendContent(splitter.splitChar(jbcClass.getMajorVersion()));

        builder.appendContent(splitter.splitChar((char) (jbcClass.getConstantPool().size() + 1)));
        for (ConstantPoolEntry constant : jbcClass.getConstantPool()) {
            constant.encode(this);
        }

        builder.appendContent(splitter.splitChar(jbcClass.getAccessFlags()));
        /* Recall that idIndex and parentIndex return indices into jbcClass.getConstantPool, which is 0-based. The
         * constant pool in the file is 1-based, however. */
        builder.appendContent(splitter.splitChar((char) (jbcClass.getIdIndex() + 1)));
        builder.appendContent(splitter.splitChar((char) (jbcClass.getParentIndex() + 1)));

        builder.appendContent(splitter.splitChar((char) jbcClass.getInterfaces().size()));
        for (Character interfaceIndex : jbcClass.getInterfaces()) {
            builder.appendContent(splitter.splitChar(interfaceIndex));
        }

        builder.appendContent(splitter.splitChar((char) jbcClass.getFields().size()));
        for (FieldEntry field : jbcClass.getFields()) {
            field.encode(this);
        }

        builder.appendContent(splitter.splitChar((char) jbcClass.getMethods().size()));
        for (MethodEntry method : jbcClass.getMethods()) {
            method.encode(this);
        }

        builder.appendContent(splitter.splitChar((char) jbcClass.getAttributes().size()));
        for (AttributeEntry attribute : jbcClass.getAttributes()) {
            attribute.encode(this);
        }
    }

    public void encodeClassConstant(byte tag, char nameIndex) {
        this.builder.appendContent(tag);
        this.builder.appendContent(this.splitter.splitChar((char) (nameIndex + 1)));
    }

    public void encodeUtf8Constant(byte tag, String value) {
        byte[] valueArray = Charset.forName("UTF-8").encode(value).array();
        // Workaround for odd bug in encoding: Sometimes there are extraneous nulls at the end
        while (valueArray[valueArray.length - 1] == 0x00) {
            valueArray = Arrays.copyOf(valueArray, valueArray.length - 1);
        }
        this.builder.appendContent(tag);
        this.builder.appendContent(this.splitter.splitChar((char) valueArray.length));
        this.builder.appendContent(valueArray);
    }

    public void encodeField(char nameIndex, char descriptorIndex, List<AttributeEntry> attributes) {
        // Access flags
        this.builder.appendContent((byte) 0x00);
        this.builder.appendContent((byte) 0x04);

        this.builder.appendContent(this.splitter.splitChar((char) (nameIndex + 1)));
        this.builder.appendContent(this.splitter.splitChar((char) (descriptorIndex + 1)));

        assert attributes.size() < Character.MAX_VALUE : "Too many attributes for Field";
        this.builder.appendContent(this.splitter.splitChar((char) attributes.size()));
        for (AttributeEntry attribute : attributes) {
            attribute.encode(this);
        }
    }

    public void encodeStaticMethod(char nameIndex, char descriptorIndex, List<AttributeEntry> attributes) {
        // Access flags
        this.builder.appendContent((byte) 0x00);
        // 0x08 (static) | 0x01 (public) = 0x09
        this.builder.appendContent((byte) 0x09);

        this.builder.appendContent(this.splitter.splitChar((char) (nameIndex + 1)));
        this.builder.appendContent(this.splitter.splitChar((char) (descriptorIndex + 1)));

        assert attributes.size() < Character.MAX_VALUE : "Too many attributes for Method";
        this.builder.appendContent(this.splitter.splitChar((char) attributes.size()));
        for (AttributeEntry attribute : attributes) {
            attribute.encode(this);
        }
    }

    public void encodeMethod(char nameIndex, char descriptorIndex, List<AttributeEntry> attributes) {
        // Access flags
        this.builder.appendContent((byte) 0x00);
        this.builder.appendContent((byte) 0x01);

        this.builder.appendContent(this.splitter.splitChar((char) (nameIndex + 1)));
        this.builder.appendContent(this.splitter.splitChar((char) (descriptorIndex + 1)));

        assert attributes.size() < Character.MAX_VALUE : "Too many attributes for Method";
        this.builder.appendContent(this.splitter.splitChar((char) attributes.size()));
        for (AttributeEntry attribute : attributes) {
            attribute.encode(this);
        }
    }

    public void encodeCodeAttribute(CodeAttribute codeAttribute) {
        this.builder.appendContent(this.splitter.splitChar((char) (codeAttribute.getAttributeNameIndex() + 1)));
        this.builder.appendContent(this.splitter.splitInt(codeAttribute.getAttributeLength()));
        this.builder.appendContent(this.splitter.splitChar(codeAttribute.getMaxStack()));
        this.builder.appendContent(this.splitter.splitChar(codeAttribute.getMaxLocals()));
        this.builder.appendContent(this.splitter.splitInt(codeAttribute.getCodeLength()));
        for (OpCode opCode : codeAttribute.getCode()) {
            opCode.encode(this);
        }
        this.builder.appendContent(this.splitter.splitChar(codeAttribute.getExceptionTableLength()));
        for (ExceptionTableEntry exception : codeAttribute.getExceptionTable()) {
            // TODO
        }
        this.builder.appendContent(this.splitter.splitChar(codeAttribute.getAttributesCount()));
        for (AttributeEntry attribute : codeAttribute.getAttributes()) {
            attribute.encode(this);
        }
    }

    public void encodeNop() {
        this.builder.appendContent((byte) 0x00);

    }

    public void encodeGoto(char target) {
        this.builder.appendContent((byte) 0xa7);
        this.builder.appendContent(this.splitter.splitChar(target));
    }

    public void encodeL2I() {
        this.builder.appendContent((byte) 0x88);
    }

    public void encodeI2L() {
        this.builder.appendContent((byte) 0x85);
    }

    public void encodeALoad(byte index) {
        this.builder.appendContent((byte) 0x19);
        this.builder.appendContent(index);
    }

    public void encodeALoad0() {
        this.builder.appendContent((byte) 0x2a);
    }

    public void encodeALoad1() {
        this.builder.appendContent((byte) 0x2b);
    }

    public void encodeALoad2() {
        this.builder.appendContent((byte) 0x2c);
    }

    public void encodeALoad3() {
        this.builder.appendContent((byte) 0x2d);
    }

    public void encodeInvokeVirtual(char index) {
        this.builder.appendContent((byte) 0xb6);
        this.builder.appendContent(this.splitter.splitChar((char) (index + 1)));
    }

    public void encodeIAdd() {
        this.builder.appendContent((byte) 0x60);
    }

    public void encodeIDiv() {
        this.builder.appendContent((byte) 0x6c);
    }

    public void encodeIMul() {
        this.builder.appendContent((byte) 0x68);
    }

    public void encodeISub() {
        this.builder.appendContent((byte) 0x64);
    }

    public void encodeNameAndType(char nameId, char typeId) {
        this.builder.appendContent((byte) 0x0c);
        this.builder.appendContent(this.splitter.splitChar((char) (nameId + 1)));
        this.builder.appendContent(this.splitter.splitChar((char) (typeId + 1)));

    }

    public void encodeMethodRef(char classRefIndex, char nameAndTypeIndex) {
        this.builder.appendContent((byte) 0x0a);
        this.builder.appendContent(this.splitter.splitChar((char) (classRefIndex + 1)));
        this.builder.appendContent(this.splitter.splitChar((char) (nameAndTypeIndex + 1)));
    }

    public void encodeStringConstant(char utf8Index) {
        this.builder.appendContent((byte) 0x08);
        this.builder.appendContent(this.splitter.splitChar((char) (utf8Index + 1)));
    }

    public void encodeAConstNullInstruction() {
        this.builder.appendContent((byte) 0x01);

    }

    public void encodeAReturnInstruction() {
        this.builder.appendContent((byte) 0xb0);
    }

    public void encodeAStore(byte varId) {
        this.builder.appendContent((byte) 0x3a);
        this.builder.appendContent(varId);

    }

    public void encodeIConst0() {
        this.builder.appendContent((byte) 0x3);
    }

    public void encodeIConst1() {
        this.builder.appendContent((byte) 0x4);
    }

    public void encodeIfEq(char target) {
        this.builder.appendContent((byte) 0x99);
        this.builder.appendContent(this.splitter.splitChar(target));
    }

    public void encodeIfIcmpLe(char target) {
        this.builder.appendContent((byte) 0xa4);
        this.builder.appendContent(this.splitter.splitChar(target));
    }

    public void encodeIfIcmpLt(char target) {
        this.builder.appendContent((byte) 0xa1);
        this.builder.appendContent(this.splitter.splitChar(target));
    }

    public void encodeIfNeq(char target) {
        this.builder.appendContent((byte) 0x9a);
        this.builder.appendContent(this.splitter.splitChar(target));
    }

    public void encodeIfNull(char target) {
        this.builder.appendContent((byte) 0x6c);
        this.builder.appendContent(this.splitter.splitChar(target));
    }

    public void encodeInstanceOf(char classRefId) {
        this.builder.appendContent((byte) 0xc1);
        this.builder.appendContent(this.splitter.splitChar((char) (classRefId + 1)));
    }

    public void encodeInvokeDynamic(char methodRefId) {
        this.builder.appendContent((byte) 0xba);
        this.builder.appendContent(this.splitter.splitChar((char) (methodRefId + 1)));
        this.builder.appendContent((byte) 0x00);
        this.builder.appendContent((byte) 0x00);
    }

    public void encodeLdc(byte constRefId) {
        this.builder.appendContent((byte) 0x12);
        this.builder.appendContent((byte) (constRefId + 1));

    }

    public void encodeNew(char classRefId) {
        this.builder.appendContent((byte) 0xbb);
        this.builder.appendContent(this.splitter.splitChar((char) (classRefId + 1)));
    }

    public void encodeSiPush(short value) {
        this.builder.appendContent((byte) 0x11);
        this.builder.appendContent(this.splitter.splitChar((char) value));
    }

    public void encodeReturn() {
        this.builder.appendContent((byte) 0xb1);
    }

    public void encodeDup() {
        this.builder.appendContent((byte) 0x59);
    }

    public void encodeInvokeSpecial(char methodRefId) {
        this.builder.appendContent((byte) 0xb7);
        this.builder.appendContent(this.splitter.splitChar((char) (methodRefId + 1)));
    }

    public void encodePop() {
        this.builder.appendContent((byte) 0x57);
    }

    public void encodeFieldRefConstant(char classRefId, char nameAndTypeRefId) {
        this.builder.appendContent((byte) 0x09);
        this.builder.appendContent(this.splitter.splitChar((char) (classRefId + 1)));
        this.builder.appendContent(this.splitter.splitChar((char) (nameAndTypeRefId + 1)));

    }
}
