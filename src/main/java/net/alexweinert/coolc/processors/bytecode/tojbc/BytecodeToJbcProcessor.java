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
import net.alexweinert.coolc.representations.jbc.JbcClass.Builder;
import net.alexweinert.coolc.representations.jbc.MethodEntry;
import net.alexweinert.coolc.representations.jbc.instructions.OpCode;

public class BytecodeToJbcProcessor extends Processor<List<ByteClass>, Collection<JbcClass>> {

    @Override
    public Collection<JbcClass> process(List<ByteClass> input) throws ProcessorException {
        final Collection<JbcClass> returnValue = new HashSet<>();
        for (ByteClass byteClass : input) {
            final JbcClass.Builder builder = new JbcClass.Builder("Cool" + byteClass.getId(), "Cool"
                    + byteClass.getParent());
            for (Attribute attr : byteClass.getAttributes()) {
                builder.addField(this.buildField(builder, attr));
            }

            builder.addMethod(this.buildInitMethod(builder, byteClass.getParent()));

            for (Method method : byteClass.getMethods()) {
                final MethodEntry jbcMethod = buildMethod(builder, byteClass, method);
                builder.addMethod(jbcMethod);
            }

            if (byteClass.getId().equals("Main")) {
                builder.addMethod(buildMainMethod(builder));
            }

            returnValue.add(builder.build());
        }

        return returnValue;
    }

    private MethodEntry buildInitMethod(Builder builder, String parent) {
        final char nameIndex = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant("<init>"));
        final char descIndex = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant("()V"));

        final MethodEntry.Builder methodBuilder = new MethodEntry.Builder(nameIndex, descIndex);

        final OpCodeAssembler assembler = new OpCodeAssembler(new JbcEncoding());
        assembler.addALoad((char) 0);

        final char parentInitDeclId = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant("()V"));
        final char parentInitNameId = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant("<init>"));
        final char parentInitNameAndDeclId = builder.addConstant(builder.getConstantBuilder().buildNameAndType(
                parentInitNameId, parentInitDeclId));

        final char parentNameId = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant("Cool" + parent));
        final char parentClassRefId = builder
                .addConstant(builder.getConstantBuilder().buildClassConstant(parentNameId));

        final char parentInitMethodRefId = builder.addConstant(builder.getConstantBuilder().buildMethodRef(
                parentClassRefId, parentInitNameAndDeclId));
        assembler.addInvokeSpecial(parentInitMethodRefId);

        assembler.addReturn();
        final char codeId = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant("Code"));
        final CodeAttribute.Builder codeBuilder = new CodeAttribute.Builder(codeId, (char) 1, (char) 1);
        codeBuilder.setCode(assembler.assemble());
        methodBuilder.addAttribute(codeBuilder.build(new JbcEncoding()));

        return methodBuilder.build();
    }

    private MethodEntry buildMainMethod(JbcClass.Builder builder) {
        final char nameIndex = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant("main"));
        final char descriptorIndex = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant(
                "([Ljava/lang/String;)V"));
        final MethodEntry.Builder methodBuilder = builder.getMethodBuilder(nameIndex, descriptorIndex);
        methodBuilder.setStatic(true);
        final OpCodeAssembler assembler = new OpCodeAssembler(new JbcEncoding());
        final char classRefNameIndex = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant("CoolMain"));
        final char classRefIndex = builder.addConstant(builder.getConstantBuilder().buildClassConstant(
                classRefNameIndex));
        assembler.addNew(classRefIndex);
        assembler.addDup();
        final char initMethodNameId = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant("<init>"));
        final char initMethodDescId = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant("()V"));
        final char initMethodNameAndDescId = builder.addConstant(builder.getConstantBuilder().buildNameAndType(
                initMethodNameId, initMethodDescId));
        final char initMethodRefId = builder.addConstant(builder.getConstantBuilder().buildMethodRef(classRefIndex,
                initMethodNameAndDescId));
        assembler.addInvokeSpecial(initMethodRefId);

        final char mainMethodNameId = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant("main"));
        final char mainMethodDescId = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant(
                "()LCoolObject;"));
        final char mainMethodTypeId = builder.addConstant(builder.getConstantBuilder().buildNameAndType(
                mainMethodNameId, mainMethodDescId));
        final char mainMethodRefId = builder.addConstant(builder.getConstantBuilder().buildMethodRef(classRefIndex,
                mainMethodTypeId));
        assembler.addInvokeVirtual(mainMethodRefId);
        assembler.addReturn();

        final char codeStringId = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant("Code"));
        final CodeAttribute.Builder codeBuilder = new CodeAttribute.Builder(codeStringId, (char) 2, (char) 2);
        codeBuilder.setCode(assembler.assemble());
        methodBuilder.addAttribute(codeBuilder.build(new JbcEncoding()));
        return methodBuilder.build();
    }

    private FieldEntry buildField(final JbcClass.Builder builder, Attribute attr) {
        final char nameIndex = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant(attr.getId()));
        final char descriptorIndex = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant(
                "L" + attr.getType() + ";"));
        final FieldEntry field = builder.getFieldBuilder(nameIndex, descriptorIndex).build();
        return field;
    }

    private MethodEntry buildMethod(final JbcClass.Builder builder, ByteClass enclosingClass, Method method) {
        final char nameIndex = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant(method.getId()));

        final String descriptor = buildMethodDescriptor(method);
        final char descriptorIndex = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant(descriptor));

        final MethodEntry.Builder methodBuilder = builder.getMethodBuilder(nameIndex, descriptorIndex);
        final CodeAttribute.Builder codeBuilder = new CodeAttribute.Builder(builder.addConstant(builder
                .getConstantBuilder().buildUtf8Constant("Code")), (char) 255, (char) (method.getParameters().size()
                + method.getLocalVars().size() + 1));

        final BytecodeOpToJbcOpConverter converter = BytecodeOpToJbcOpConverter.create(enclosingClass,
                method.getParameters(), method.getLocalVars(), builder, JbcEncoding.createStandardEncoding());
        final List<OpCode> opCodes = converter.convert(method.getInstruction());
        codeBuilder.setCode(opCodes);

        methodBuilder.addAttribute(codeBuilder.build(JbcEncoding.createStandardEncoding()));
        return methodBuilder.build();
    }

    private String buildMethodDescriptor(Method method) {
        final StringBuilder descriptorBuilder = new StringBuilder("(");
        for (TypedId id : method.getParameters()) {
            descriptorBuilder.append("L");
            descriptorBuilder.append("Cool" + id.getType());
            descriptorBuilder.append(";");
        }
        descriptorBuilder.append(")");
        if (method.getReturnType() != null) {
            descriptorBuilder.append("L");
            descriptorBuilder.append("Cool" + method.getReturnType());
            descriptorBuilder.append(";");
        } else {
            descriptorBuilder.append("V");
        }
        return descriptorBuilder.toString();
    }
}
