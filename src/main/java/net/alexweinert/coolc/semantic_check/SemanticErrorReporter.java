package net.alexweinert.coolc.semantic_check;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.ast.ObjectReference;
import net.alexweinert.coolc.program.ast.TreeNode;
import net.alexweinert.coolc.program.symboltables.IdSymbol;

class SemanticErrorReporter {
    private final Output out;

    SemanticErrorReporter(Output out) {
        this.out = out;
    }

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

    public void reportRedefinitionOfBuiltInClass(IdSymbol identifier, Class classNode) {
        final String errorString = String.format("Redefinition of builtin class %s at %s:%d", identifier,
                classNode.getFilename(), classNode.getLineNumber());
        out.error(errorString);
    }

    public void reportBaseClassInheritance(Class classNode) {
        final String formatString = "Class %s inherits from base class %s at %s:%d\nIgnoring inheritance declaration";
        out.error(String.format(formatString, classNode.getIdentifier(), classNode.getParent(),
                classNode.getFilename(), classNode.getLineNumber()));
    }

    public void reportUndefinedParentClass(Class classNode) {
        final String formatString = "Parent %s of class %s is undefined. Defined at %s:%d\nSetting parent to Object";
        out.error(String.format(formatString, classNode.getParent(), classNode.getIdentifier(),
                classNode.getFilename(), classNode.getLineNumber()));
    }

    public void reportCircularInheritance(Set<Class> inheritanceCircle, Class tieBreaker) {
        StringBuilder builder = new StringBuilder("Detected circular inheritance:\n");
        final Class firstClass = inheritanceCircle.iterator().next();
        final Iterator<Class> circleIterator = inheritanceCircle.iterator();
        while (circleIterator.hasNext()) {
            final Class currentClass = circleIterator.next();
            builder.append("  class ");
            builder.append(currentClass.getIdentifier());
            builder.append(" (defined at ");
            builder.append(currentClass.getFilename());
            builder.append(":");
            builder.append(currentClass.getLineNumber());
            builder.append(") inherits from\n");
        }
        builder.append("  class ");
        builder.append(firstClass.getIdentifier());
        builder.append(" (defined at ");
        builder.append(firstClass.getFilename());
        builder.append(":");
        builder.append(firstClass.getLineNumber());
        builder.append(")\n");
        builder.append("Breaking circle by setting parent of ");
        builder.append(tieBreaker.getIdentifier());
        builder.append(" to Object");
        out.error(builder.toString());
    }

    public void reportClassRedefinition(Class originalClass, Class redefinedClass) {
        final String formatString = "Redefinition of class %s at %s:%d. Original definition at %s:%d. Ignoring redefinition";
        out.error(String.format(formatString, originalClass.getIdentifier(), redefinedClass.getFilename(),
                redefinedClass.getLineNumber(), originalClass.getFilename(), originalClass.getLineNumber()));
    }

    public void reportAttributeInitializerTypeError(Attribute attribute, ExpressionType initializerType) {
        final String formatString = "Type error in initialization of attribute %s at %s:%d\n  Declared Type: %s\n  Type of initializer: %s";
        out.error(String.format(formatString, attribute.getName(), attribute.getFilename(), attribute.getLineNumber(),
                attribute.getDeclaredType(), initializerType));

    }

    public void reportMethodBodyTypeError(Method method, ExpressionType resultType) {
        final String formatString = "Type error in definition of method %s at %s:%d\n  Declared Type: %s\n  Type of body expression: %s";
        out.error(String.format(formatString, method.getName(), method.getFilename(), method.getLineNumber(),
                method.getReturnType(), resultType));

    }

    public void reportTypeMismatch(TreeNode expression, IdSymbol actualType, IdSymbol expexctedType) {
        final String formatString = "Type mismatch in operands of expression at %s:%d\n  Expected Type: %s\n  Actual Type: %s\n";
        out.error(String.format(formatString, expression.getFilename(), expression.getLineNumber(), expexctedType,
                actualType));
    }

    public void reportVariableOutOfScope(ObjectReference reference) {
        final String formatString = "Variable %s does not exist in current scope at %s:%d";
        out.error(String.format(formatString, reference.getVariableIdentifier(), reference.getFilename(),
                reference.getLineNumber()));

    }
}
