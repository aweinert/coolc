package net.alexweinert.coolc.processors.bytecode.tostring;

import java.util.Collection;
import java.util.Iterator;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.bytecode.Attribute;
import net.alexweinert.coolc.representations.bytecode.ByteClass;
import net.alexweinert.coolc.representations.bytecode.LabeledInstruction;
import net.alexweinert.coolc.representations.bytecode.Method;
import net.alexweinert.coolc.representations.bytecode.TypedId;

public class ToStringProcessor extends Processor<Collection<ByteClass>, String> {

    @Override
    public String process(Collection<ByteClass> input) throws ProcessorException {
        final StringBuilder builder = new StringBuilder();

        for (ByteClass byteClass : input) {
            builder.append(String.format("class %s inherits %s:\n", byteClass.getId(), byteClass.getParent()));
            builder.append("-attributes:\n");
            for (Attribute attribute : byteClass.getAttributes()) {
                this.unparseAttribute(builder, attribute);
            }
            builder.append("\n");
            builder.append("-methods:\n");
            for (Method method : byteClass.getMethods()) {
                this.unparseMethod(builder, method);
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    private void unparseAttribute(StringBuilder builder, Attribute attribute) {
        builder.append(String.format("--%s %s = %s();\n", attribute.getType(), attribute.getId(),
                attribute.getInitMethodId()));
    }

    private void unparseMethod(StringBuilder builder, Method method) {
        builder.append(String.format("--%s %s(", method.getReturnType(), method.getId()));
        final Iterator<TypedId> argIt = method.getParameters().iterator();
        while (argIt.hasNext()) {
            final TypedId arg = argIt.next();
            builder.append(String.format("%s %s", arg.getType(), arg.getId()));
            if (argIt.hasNext()) {
                builder.append(", ");
            }
            builder.append("):\n");
        }
        builder.append("---local variables\n");
        for (TypedId local : method.getLocalVars()) {
            builder.append(String.format("    %s %s = null;\n", local.getType(), local.getId()));
        }
        builder.append("---instructions\n");
        for (LabeledInstruction instruction : method.getInstruction()) {
            builder.append(String.valueOf(instruction) + "\n");
        }
    }
}
