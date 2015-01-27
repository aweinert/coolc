package net.alexweinert.coolc.semantic_check;

import java.util.List;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;

class DoubleFeatureErrorReporter {
    final private Output output = new Output();

    public void multipleAttributes(AbstractSymbol classIdentifier, List<Attribute> definitions) {
        final StringBuilder builder = new StringBuilder();
        builder.append("ERROR: Multiple definitions of attribute ");
        builder.append(definitions.get(0).getName());
        builder.append(" in class ");
        builder.append(classIdentifier);
        builder.append(" at the following locations:\n");
        for (Attribute attribute : definitions) {
            builder.append("  ");
            builder.append(attribute.getFilename());
            builder.append(":");
            builder.append(attribute.getLineNumber());
            builder.append("\n");
        }
        builder.append("  Only using the first one, ignoring subsequent ones");
        output.error(builder.toString());
    }

    public void multipleMethods(AbstractSymbol classIdentifier, List<Method> definitions) {
        final StringBuilder builder = new StringBuilder();
        builder.append("ERROR: Multiple definitions of method ");
        builder.append(definitions.get(0).getName());
        builder.append(" in class ");
        builder.append(classIdentifier);
        builder.append(" at the following locations:\n");
        for (Method method : definitions) {
            builder.append("  ");
            builder.append(method.getFilename());
            builder.append(":");
            builder.append(method.getLineNumber());
            builder.append("\n");
        }
        builder.append("  Only using the first one, ignoring subsequent ones");
        output.error(builder.toString());
    }
}
