package net.alexweinert.coolc.processors.bytecode.tojbc;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.bytecode.Attribute;
import net.alexweinert.coolc.representations.bytecode.ByteClass;
import net.alexweinert.coolc.representations.bytecode.Method;
import net.alexweinert.coolc.representations.bytecode.TypedId;
import net.alexweinert.coolc.representations.jbc.JbcClass;

public class BytecodeToJbcProcessor extends Processor<List<ByteClass>, Collection<JbcClass>> {

    @Override
    public Collection<JbcClass> process(List<ByteClass> input) throws ProcessorException {
        final Collection<JbcClass> returnValue = new HashSet<>();
        for (ByteClass byteClass : input) {
            final JbcClass.Builder builder = new JbcClass.Builder(byteClass.getId(), byteClass.getParent());
            for (Attribute attr : byteClass.getAttributes()) {
                final char nameIndex = builder
                        .addConstant(builder.getConstantBuilder().buildUtf8Constant(attr.getId()));
                final char descriptorIndex = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant(
                        "L" + attr.getType() + ";"));
                builder.addField(builder.getFieldBuilder(nameIndex, descriptorIndex).build());
            }

            for (Method method : byteClass.getMethods()) {
                final char nameIndex = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant(
                        method.getId()));

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
                final char descriptorIndex = builder.addConstant(builder.getConstantBuilder().buildUtf8Constant(
                        descriptorBuilder.toString()));
                builder.addMethod(builder.getMethodBuilder(nameIndex, descriptorIndex).build());
            }

            returnValue.add(builder.build());
        }

        return returnValue;
    }
}
