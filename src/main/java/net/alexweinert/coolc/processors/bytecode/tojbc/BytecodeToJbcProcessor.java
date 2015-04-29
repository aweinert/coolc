package net.alexweinert.coolc.processors.bytecode.tojbc;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;
import net.alexweinert.coolc.representations.bytecode.Attribute;
import net.alexweinert.coolc.representations.bytecode.ByteClass;
import net.alexweinert.coolc.representations.bytecode.Method;
import net.alexweinert.coolc.representations.bytecode.TypedId;
import net.alexweinert.coolc.representations.jbc.CodeAttribute;
import net.alexweinert.coolc.representations.jbc.FieldEntry;
import net.alexweinert.coolc.representations.jbc.JbcClass;
import net.alexweinert.coolc.representations.jbc.MethodEntry;
import net.alexweinert.coolc.representations.jbc.instructions.OpCode;

public class BytecodeToJbcProcessor extends Processor<List<ByteClass>, Collection<JbcClass>> {

    @Override
    public Collection<JbcClass> process(List<ByteClass> input) throws ProcessorException {
        final Collection<JbcClass> returnValue = new HashSet<>();
        for (ByteClass byteClass : input) {
            final JbcClass.Builder builder = new JbcClass.Builder(byteClass.getId(), byteClass.getParent());
            for (Attribute attr : byteClass.getAttributes()) {
                builder.addField(this.buildField(builder, attr));
            }

            for (Method method : byteClass.getMethods()) {
                final MethodEntry jbcMethod = buildMethod(builder, method);
                builder.addMethod(jbcMethod);
            }

            returnValue.add(builder.build());
        }

        return returnValue;
    }

    private FieldEntry buildField(final JbcClass.Builder builder, Attribute attr) {
        final char nameIndex = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant(attr.getId()));
        final char descriptorIndex = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant(
                "L" + attr.getType() + ";"));
        final FieldEntry field = builder.getFieldBuilder(nameIndex, descriptorIndex).build();
        return field;
    }

    private MethodEntry buildMethod(final JbcClass.Builder builder, Method method) {
        final char nameIndex = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant(method.getId()));

        final String descriptor = buildMethodDescriptor(method);
        final char descriptorIndex = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant(descriptor));

        final MethodEntry.Builder methodBuilder = builder.getMethodBuilder(nameIndex, descriptorIndex);
        final CodeAttribute.Builder codeBuilder = new CodeAttribute.Builder(builder.addConstant(builder
                .getConstantBuilder().buildUtf8Constant("Code")), (char) 255,
                (char) (method.getParameters().size() + method.getLocalVars().size()));

        final BytecodeOpToJbcOpConverter converter = BytecodeOpToJbcOpConverter.create(method.getParameters(),
                method.getLocalVars(), builder, JbcEncoding.createStandardEncoding());
        final List<OpCode> opCodes = converter.convert(method.getInstruction());
        codeBuilder.setCode(opCodes);

        methodBuilder.addAttribute(codeBuilder.build(JbcEncoding.createStandardEncoding()));
        return methodBuilder.build();
    }

    private String buildMethodDescriptor(Method method) {
        final StringBuilder descriptorBuilder = new StringBuilder("(");
        for (TypedId id : method.getParameters()) {
            descriptorBuilder.append("L");
            descriptorBuilder.append(id.getType());
            descriptorBuilder.append(";");
        }
        descriptorBuilder.append(")");
        descriptorBuilder.append("L");
        descriptorBuilder.append(method.getReturnType());
        descriptorBuilder.append(";");
        return descriptorBuilder.toString();
    }
}
