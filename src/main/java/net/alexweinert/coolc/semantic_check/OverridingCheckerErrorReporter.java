package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.Method;

class OverridingCheckerErrorReporter {
    final private Output out;

    OverridingCheckerErrorReporter(Output out) {
        this.out = out;
    }

    void reportOverriddenAttribute(Attribute offendingAttribute, Attribute originalAttribute) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Redefined attribute at ");
        builder.append(offendingAttribute.getFilename());
        builder.append(":");
        builder.append(offendingAttribute.getLineNumber());
        builder.append(".\n");
        builder.append("Original definition at ");
        builder.append(originalAttribute.getFilename());
        builder.append(":");
        builder.append(originalAttribute.getLineNumber());
        builder.append(".");
        out.error(builder.toString());
    }

    void reportWronglyOverriddenMethod(Method offendingMethod, Method originalMethod) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Wrongly overridden method at ");
        builder.append(offendingMethod.getFilename());
        builder.append(":");
        builder.append(offendingMethod.getLineNumber());
        builder.append(".\n");
        builder.append("Original definition at ");
        builder.append(originalMethod.getFilename());
        builder.append(":");
        builder.append(originalMethod.getLineNumber());
        builder.append(".");
        out.error(builder.toString());
    }
}
