package net.alexweinert.coolc.processors.cool.hierarchycheck;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.alexweinert.coolc.representations.cool.ast.Attribute;
import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Method;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import org.springframework.stereotype.Component;

@Component
class SemanticErrorReporter {
    final List<String> errorMessages = new LinkedList<>();

    public void reportMultipleAttributes(ClassNode classNode, List<Attribute> attributes) {
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
        this.errorMessages.add(builder.toString());
    }

    public void reportMultipleMethods(ClassNode classNode, List<Method> methods) {
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
        this.errorMessages.add(builder.toString());
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
        this.errorMessages.add(builder.toString());

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
        this.errorMessages.add(builder.toString());
    }

    public void reportRedefinitionOfBuiltInClass(IdSymbol identifier, ClassNode classNode) {
        final String errorString = String.format("Redefinition of builtin class %s at %s:%d", identifier,
                classNode.getFilename(), classNode.getLineNumber());
        this.errorMessages.add(errorString);
    }

    public void reportBaseClassInheritance(ClassNode classNode) {
        final String formatString = "Class %s inherits from base class %s at %s:%d\nIgnoring inheritance declaration";
        this.errorMessages.add(String.format(formatString, classNode.getIdentifier(), classNode.getParent(),
                classNode.getFilename(), classNode.getLineNumber()));
    }

    public void reportUndefinedParentClass(ClassNode classNode) {
        final String formatString = "Parent %s of class %s is undefined. Defined at %s:%d\nSetting parent to Object";
        this.errorMessages.add(String.format(formatString, classNode.getParent(), classNode.getIdentifier(),
                classNode.getFilename(), classNode.getLineNumber()));
    }

    public void reportCircularInheritance(Set<ClassNode> inheritanceCircle, ClassNode tieBreaker) {
        StringBuilder builder = new StringBuilder("Detected circular inheritance:\n");
        final ClassNode firstClass = inheritanceCircle.iterator().next();
        final Iterator<ClassNode> circleIterator = inheritanceCircle.iterator();
        while (circleIterator.hasNext()) {
            final ClassNode currentClass = circleIterator.next();
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
        this.errorMessages.add(builder.toString());
    }

    public void reportClassRedefinition(ClassNode originalClass, ClassNode redefinedClass) {
        final String formatString = "Redefinition of class %s at %s:%d. Original definition at %s:%d. Ignoring redefinition";
        this.errorMessages.add(String.format(formatString, originalClass.getIdentifier(), redefinedClass.getFilename(),
                redefinedClass.getLineNumber(), originalClass.getFilename(), originalClass.getLineNumber()));
    }

    public boolean hasErrors() {
        return !this.errorMessages.isEmpty();
    }

    public List<String> getErrors() {
        return this.errorMessages;
    }

}
