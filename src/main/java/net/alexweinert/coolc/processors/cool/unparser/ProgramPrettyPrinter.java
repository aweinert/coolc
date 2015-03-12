package net.alexweinert.coolc.processors.cool.unparser;

import java.util.Iterator;

import net.alexweinert.coolc.representations.cool.program.parsed.Attribute;
import net.alexweinert.coolc.representations.cool.program.parsed.ClassNode;
import net.alexweinert.coolc.representations.cool.program.parsed.Classes;
import net.alexweinert.coolc.representations.cool.program.parsed.Features;
import net.alexweinert.coolc.representations.cool.program.parsed.Formal;
import net.alexweinert.coolc.representations.cool.program.parsed.Method;
import net.alexweinert.coolc.representations.cool.program.parsed.ParsedProgramVisitor;
import net.alexweinert.coolc.representations.cool.program.parsed.Program;

class ProgramPrettyPrinter extends ParsedProgramVisitor {
    private StringBuilder stringBuilder = new StringBuilder();
    private UntypedExpressionPrettyPrinter expressionPrinter;

    public String printProgram(Program program) {
        this.stringBuilder = new StringBuilder();
        this.expressionPrinter = new UntypedExpressionPrettyPrinter(this.stringBuilder);

        program.acceptVisitor(this);
        return this.stringBuilder.toString();
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        stringBuilder.append(attribute.getName());
        stringBuilder.append(": ");
        stringBuilder.append(attribute.getDeclaredType());
        stringBuilder.append(" <- ");
        attribute.getInitializer().acceptVisitor(this.expressionPrinter);
        stringBuilder.append(";");
    }

    @Override
    public void visitClassPreorder(ClassNode classNode) {
        stringBuilder.append("class ");
        stringBuilder.append(classNode.getIdentifier());
        stringBuilder.append(" inherits ");
        stringBuilder.append(classNode.getParent());
        stringBuilder.append(" {\n");
    }

    @Override
    public void visitClassPostorder(ClassNode classNode) {
        stringBuilder.append("}");
    }

    @Override
    public void visitClassesPreorder(Classes classes) {}

    @Override
    public void visitClassesInorder(Classes classes) {
        stringBuilder.append(";\n\n");
    }

    @Override
    public void visitClassesPostorder(Classes classes) {}

    @Override
    public void visitFeaturesPreorder(Features features) {}

    @Override
    public void visitFeaturesInorder(Features features) {
        stringBuilder.append(";\n\n");
    }

    @Override
    public void visitFeaturesPostorder(Features features) {
        // Finish the last feature with a ; as well
        stringBuilder.append(";");
    }

    @Override
    public void visitMethod(Method method) {
        stringBuilder.append(method.getName());
        stringBuilder.append("(");

        final Iterator<Formal> formalIterator = method.getFormals().iterator();
        while (formalIterator.hasNext()) {
            final Formal currentFormal = formalIterator.next();
            stringBuilder.append(currentFormal.getIdentifier());
            stringBuilder.append(": ");
            stringBuilder.append(currentFormal.getDeclaredType());
            if (formalIterator.hasNext()) {
                stringBuilder.append(stringBuilder.append(", "));
            }
        }

        stringBuilder.append("): ");
        stringBuilder.append(method.getReturnType());
        stringBuilder.append(" {\n");

        method.getBody().acceptVisitor(this.expressionPrinter);

        stringBuilder.append("}");
    }

    @Override
    public void visitProgramPreorder(Program program) {}

    @Override
    public void visitProgramPostorder(Program program) {}

}
