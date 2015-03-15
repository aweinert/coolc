package net.alexweinert.coolc.processors.java;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Stack;

import net.alexweinert.coolc.infrastructure.Backend;
import net.alexweinert.coolc.representations.cool.ast.Addition;
import net.alexweinert.coolc.representations.cool.ast.ArithmeticNegation;
import net.alexweinert.coolc.representations.cool.ast.Attribute;
import net.alexweinert.coolc.representations.cool.ast.BoolConst;
import net.alexweinert.coolc.representations.cool.ast.BooleanNegation;
import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Division;
import net.alexweinert.coolc.representations.cool.ast.Equality;
import net.alexweinert.coolc.representations.cool.ast.Formal;
import net.alexweinert.coolc.representations.cool.ast.IntConst;
import net.alexweinert.coolc.representations.cool.ast.IsVoid;
import net.alexweinert.coolc.representations.cool.ast.LessThan;
import net.alexweinert.coolc.representations.cool.ast.LessThanOrEquals;
import net.alexweinert.coolc.representations.cool.ast.Method;
import net.alexweinert.coolc.representations.cool.ast.Multiplication;
import net.alexweinert.coolc.representations.cool.ast.ObjectReference;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.ast.StringConst;
import net.alexweinert.coolc.representations.cool.ast.Subtraction;
import net.alexweinert.coolc.representations.cool.ast.Visitor;

public class JavaBackend extends Visitor implements Backend<Program> {

    private final Path pathToFolder;
    private final NameGenerator nameGen = new NameGenerator();
    private final Stack<String> variables = new Stack<>();

    private ExceptionCatchingFileWriter writer;

    public JavaBackend(Path pathToFolder) {
        this.pathToFolder = pathToFolder;
    }

    @Override
    public void process(Program input) {
        input.acceptVisitor(this);
    }

    @Override
    public void visitClassPreorder(ClassNode classNode) {
        final Path pathToFile = pathToFolder.resolve(this.nameGen.getJavaNameForClass(classNode.getIdentifier())
                + ".java");
        try {
            this.writer = new ExceptionCatchingFileWriter(new FileWriter(pathToFile.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.writer.write("public class " + this.nameGen.getJavaNameForClass(classNode.getIdentifier()) + " {\n");
    }

    @Override
    public void visitClassPostorder(ClassNode classNode) {
        this.writer.write("}");
        this.writer.close();
    }

    @Override
    public void visitAttributePreorder(Attribute attribute) {
        writer.write("protected " + this.nameGen.getJavaNameForClass(attribute.getDeclaredType()) + " "
                + this.nameGen.getJavaNameForVariable(attribute.getName()) + ";\n");
        writer.write("{");
    }

    @Override
    public void visitAttributePostorder(Attribute attribute) {
        writer.write(this.nameGen.getJavaNameForVariable(attribute.getName()) + " = " + this.variables.pop() + ";");
        writer.write("}\n\n");
    }

    @Override
    public void visitMethodPreorder(Method method) {
        writer.write("public " + this.nameGen.getJavaNameForClass(method.getReturnType()) + " " + method.getName()
                + "(");
        final Iterator<Formal> iterator = method.getFormals().iterator();
        while (iterator.hasNext()) {
            final Formal formal = iterator.next();
            writer.write(formal.getDeclaredType() + " " + formal.getIdentifier());
            if (iterator.hasNext()) {
                writer.write(", ");
            }
        }
        writer.write(") {\n");
    }

    @Override
    public void visitMethodPostorder(Method method) {
        writer.write("}\n\n");
    }

    @Override
    public void visitBoolConst(BoolConst boolConst) {
        if (boolConst.getValue()) {
            this.variables.push("true");
        } else {
            this.variables.push("false");
        }
    }

    @Override
    public void visitIntConst(IntConst intConst) {
        this.variables.push(intConst.getValue().toString());
    }

    @Override
    public void visitObjectReference(ObjectReference objectReference) {
        this.variables.push(this.nameGen.getJavaNameForVariable(objectReference.getVariableIdentifier()));
    }

    @Override
    public void visitStringConstant(StringConst stringConst) {
        this.variables.push(stringConst.getValue().getString());
    }

    @Override
    public void visitAdditionPostorder(Addition addition) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        writer.write("int " + resultVariable + " = " + lhsVariable + " + " + rhsVariable + ";");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitBooleanNegationPostorder(BooleanNegation booleanNegation) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String argVariable = this.variables.pop();
        writer.write("boolean " + resultVariable + " = !" + argVariable + ";");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitDivisionPostorder(Division division) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        writer.write("int " + resultVariable + " = " + lhsVariable + " / " + rhsVariable + ";");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitEqualityPostorder(Equality equality) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        writer.write("boolean " + resultVariable + " = (" + lhsVariable + " == " + rhsVariable + ");");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitIsVoidPostorder(IsVoid isVoid) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String argVariable = this.variables.pop();
        writer.write("boolean " + resultVariable + " = (" + argVariable + " == null);");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitLessThanPostorder(LessThan lessThan) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        writer.write("boolean " + resultVariable + " = (" + lhsVariable + " < " + rhsVariable + ");");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitLessThanOrEqualsPostorder(LessThanOrEquals lessThanOrEquals) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        writer.write("boolean " + resultVariable + " = (" + lhsVariable + " <= " + rhsVariable + ");");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitMultiplicationPostorder(Multiplication multiplication) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        writer.write("int " + resultVariable + " = (" + lhsVariable + " * " + rhsVariable + ");");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitSubtractionPostorder(Subtraction subtraction) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        writer.write("int " + resultVariable + " = (" + lhsVariable + " - " + rhsVariable + ");");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitArithmeticNegationPostOrder(ArithmeticNegation arithmeticNegation) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String argVariable = this.variables.pop();
        writer.write("int " + resultVariable + " = -" + argVariable + ";");
        this.variables.push(resultVariable);
    }
}
