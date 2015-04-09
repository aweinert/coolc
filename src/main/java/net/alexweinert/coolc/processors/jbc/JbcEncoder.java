package net.alexweinert.coolc.processors.jbc;

import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;

import net.alexweinert.coolc.representations.io.File;
import net.alexweinert.coolc.representations.io.File.Builder;
import net.alexweinert.coolc.representations.jbc.AttributeEntry;
import net.alexweinert.coolc.representations.jbc.ClassConstant;
import net.alexweinert.coolc.representations.jbc.ConstantPoolEntry;
import net.alexweinert.coolc.representations.jbc.FieldEntry;
import net.alexweinert.coolc.representations.jbc.JbcClass;
import net.alexweinert.coolc.representations.jbc.MethodEntry;
import net.alexweinert.coolc.representations.jbc.Utf8Constant;

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
            builder.appendContent(method.toBytes());
        }

        builder.appendContent(splitter.splitChar((char) jbcClass.getAttributes().size()));
        for (AttributeEntry attribute : jbcClass.getAttributes()) {
            builder.appendContent(attribute.toBytes());
        }
    }

    public void encodeClassConstant(byte tag, char nameIndex) {
        this.builder.appendContent(tag);
        this.builder.appendContent(this.splitter.splitChar((char) (nameIndex + 1)));
    }

    public void encodeUtf8Constant(byte tag, String value) {
        final byte[] valueArray = Charset.forName("UTF-8").encode(value).array();
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
            this.builder.appendContent(attribute.toBytes());
        }
    }
}
