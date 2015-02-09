package net.alexweinert.coolc.semantic_check;

import java.util.List;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.symboltables.IdSymbol;

class SemanticErrorReporter implements ISemanticErrorReporter {
    private final Output out;

    SemanticErrorReporter(Output out) {
        this.out = out;
    }

    @Override
    public void reportMultipleAttributes(Class classNode, List<Attribute> attributes) {
        final StringBuilder builder = new StringBuilder();
        builder.append("ERROR: Multiple definitions of attribute ");
        builder.append(attributes.get(0).getName());
        builder.append(" in class ");
        builder.append(classNode.getIdentifier());
        builder.append(" at the following locations:\n");
        for (Attribute attribute : attributes) {
            builder.append("  ");
            builder.append(attribute.getFilename());
            builder.append(":");
            builder.append(attribute.getLineNumber());
            builder.append("\n");
        }
        builder.append("  Only using the first one, ignoring subsequent ones");
        out.error(builder.toString());
    }

    @Override
    public void reportMultipleMethods(Class classNode, List<Method> methods) {
        final StringBuilder builder = new StringBuilder();
        builder.append("ERROR: Multiple definitions of method ");
        builder.append(methods.get(0).getName());
        builder.append(" in class ");
        builder.append(classNode.getIdentifier());
        builder.append(" at the following locations:\n");
        for (Method method : methods) {
            builder.append("  ");
            builder.append(method.getFilename());
            builder.append(":");
            builder.append(method.getLineNumber());
            builder.append("\n");
        }
        builder.append("  Only using the first one, ignoring subsequent ones");
        out.error(builder.toString());
    }

    @Override
    public void reportOverriddenAttribute(Attribute originalAttribute, Attribute offendingAttribute) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Redefined attribute at ");
        builder.append(offendingAttribute.getFilename());
        builder.append(":");
        builder.append(offendingAttribute.getLineNumber());
        builder.append("\n");
        builder.append("Original definition at ");
        builder.append(originalAttribute.getFilename());
        builder.append(":");
        builder.append(originalAttribute.getLineNumber());
        out.error(builder.toString());

    }

    @Override
    public void reportWronglyOverriddenMethod(Method originalMethod, Method offendingMethod) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Wrongly overridden method at ");
        builder.append(offendingMethod.getFilename());
        builder.append(":");
        builder.append(offendingMethod.getLineNumber());
        builder.append("\n");
        builder.append("Original definition at ");
        builder.append(originalMethod.getFilename());
        builder.append(":");
        builder.append(originalMethod.getLineNumber());
        out.error(builder.toString());
    }

    @Override
    public void reportRedefinitionOfBuiltInClass(IdSymbol identifier, Class classNode) {
        final String errorString = String.format("Redefinition of builtin class %s at %s:%d", identifier,
                classNode.getFilename(), classNode.getLineNumber());
        out.error(errorString);
    }

    @Override
    public void reportBaseClassInheritance(Class classNode) {
        final String formatString = "Class %s inherits from base class %s at %s:%d\nIgnoring inheritance declaration";
        out.error(String.format(formatString, classNode.getIdentifier(), classNode.getParent(),
                classNode.getFilename(), classNode.getLineNumber()));
    }

    @Override
    public void reportUndefinedParentClass(Class classNode) {
        final String formatString = "Parent %s of class %s is undefined. Defined at %s:%d\nSetting parent to Object";
        out.error(String.format(formatString, classNode.getParent(), classNode.getIdentifier(),
                classNode.getFilename(), classNode.getLineNumber()));
    }
}
