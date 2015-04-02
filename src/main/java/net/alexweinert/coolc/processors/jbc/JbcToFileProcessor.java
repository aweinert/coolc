package net.alexweinert.coolc.processors.jbc;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.io.File;
import net.alexweinert.coolc.representations.jbc.AttributeEntry;
import net.alexweinert.coolc.representations.jbc.ClassConstant;
import net.alexweinert.coolc.representations.jbc.ConstantPoolEntry;
import net.alexweinert.coolc.representations.jbc.FieldEntry;
import net.alexweinert.coolc.representations.jbc.JbcClass;
import net.alexweinert.coolc.representations.jbc.MethodEntry;
import net.alexweinert.coolc.representations.jbc.Utf8Constant;

public class JbcToFileProcessor extends Processor<Collection<JbcClass>, Collection<File>> {

    private final ByteSplitter splitter = new ByteSplitter();

    @Override
    public Collection<File> process(Collection<JbcClass> input) throws ProcessorException {
        final Collection<File> returnValue = new HashSet<>();
        for (JbcClass jbcClass : input) {
            final ClassConstant thisConstant = (ClassConstant) jbcClass.getConstantPool().get(jbcClass.getIdIndex());
            final Utf8Constant thisId = (Utf8Constant) jbcClass.getConstantPool().get(thisConstant.getNameIndex());
            final File.Builder builder = new File.Builder(Paths.get(thisId.getValue() + ".class"));

            builder.appendContent(splitter.splitInt(0xCAFEBABE));
            builder.appendContent(splitter.splitChar(jbcClass.getMinorVersion()));
            builder.appendContent(splitter.splitChar(jbcClass.getMajorVersion()));

            builder.appendContent(splitter.splitChar((char) (jbcClass.getConstantPool().size() + 1)));
            for (ConstantPoolEntry constant : jbcClass.getConstantPool()) {
                builder.appendContent(constant.toBytes());
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
                builder.appendContent(field.toBytes());
            }

            builder.appendContent(splitter.splitChar((char) jbcClass.getMethods().size()));
            for (MethodEntry method : jbcClass.getMethods()) {
                builder.appendContent(method.toBytes());
            }

            builder.appendContent(splitter.splitChar((char) jbcClass.getAttributes().size()));
            for (AttributeEntry attribute : jbcClass.getAttributes()) {
                builder.appendContent(attribute.toBytes());
            }

            returnValue.add(builder.build());
        }
        return returnValue;
    }
}
