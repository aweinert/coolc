package net.alexweinert.coolc.processors.java.unparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Stack;

import net.alexweinert.coolc.infrastructure.Backend;
import net.alexweinert.coolc.representations.cool.ast.Addition;
import net.alexweinert.coolc.representations.cool.ast.ArithmeticNegation;
import net.alexweinert.coolc.representations.cool.ast.Assign;
import net.alexweinert.coolc.representations.cool.ast.Attribute;
import net.alexweinert.coolc.representations.cool.ast.BlockExpressions;
import net.alexweinert.coolc.representations.cool.ast.BoolConst;
import net.alexweinert.coolc.representations.cool.ast.BooleanNegation;
import net.alexweinert.coolc.representations.cool.ast.Case;
import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Division;
import net.alexweinert.coolc.representations.cool.ast.Equality;
import net.alexweinert.coolc.representations.cool.ast.Formal;
import net.alexweinert.coolc.representations.cool.ast.FunctionCall;
import net.alexweinert.coolc.representations.cool.ast.If;
import net.alexweinert.coolc.representations.cool.ast.IntConst;
import net.alexweinert.coolc.representations.cool.ast.IsVoid;
import net.alexweinert.coolc.representations.cool.ast.LessThan;
import net.alexweinert.coolc.representations.cool.ast.LessThanOrEquals;
import net.alexweinert.coolc.representations.cool.ast.Let;
import net.alexweinert.coolc.representations.cool.ast.Loop;
import net.alexweinert.coolc.representations.cool.ast.Method;
import net.alexweinert.coolc.representations.cool.ast.Multiplication;
import net.alexweinert.coolc.representations.cool.ast.New;
import net.alexweinert.coolc.representations.cool.ast.ObjectReference;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.ast.StaticFunctionCall;
import net.alexweinert.coolc.representations.cool.ast.StringConst;
import net.alexweinert.coolc.representations.cool.ast.Subtraction;
import net.alexweinert.coolc.representations.cool.ast.Typecase;
import net.alexweinert.coolc.representations.cool.ast.Visitor;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

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
        this.copyResource("CoolObject.java");
        this.copyResource("CoolBool.java");
        this.copyResource("CoolInt.java");
        this.copyResource("CoolString.java");
        this.copyResource("CoolIO.java");
        input.acceptVisitor(this);
    }

    private void copyResource(String fileName) {
        File sourceFile = new File(this.getClass().getClassLoader().getResource(fileName).getFile());
        File destFile = new File(this.pathToFolder.resolve(fileName).toString());
        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (classNode.getIdentifier().equals(IdTable.getInstance().getMainSymbol())) {
            this.writer.write("public static void main(String[] args) {\n");
            this.writer.write("final CoolMain main = new CoolMain();\n");
            this.writer.write("main.coolmain();\n");
            this.writer.write("}\n");
        }
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
            writer.write(this.nameGen.getJavaNameForClass(formal.getDeclaredType()) + " "
                    + this.nameGen.getJavaNameForVariable(formal.getIdentifier()));
            if (iterator.hasNext()) {
                writer.write(", ");
            }
        }
        writer.write(") {\n");
    }

    @Override
    public void visitMethodPostorder(Method method) {
        writer.write("return " + this.variables.pop() + ";");
        writer.write("}\n\n");
    }

    @Override
    public void visitBoolConst(BoolConst boolConst) {
        final String varName = this.nameGen.getFreshVariableName();
        this.writer.write("CoolBool " + varName + " = new CoolBool(" + boolConst.getValue().toString() + ");\n");
        this.variables.push(varName);
    }

    @Override
    public void visitIntConst(IntConst intConst) {
        final String varName = this.nameGen.getFreshVariableName();
        this.writer.write("CoolInt " + varName + " = new CoolInt(" + intConst.getValue().toString() + ");\n");
        this.variables.push(varName);
    }

    @Override
    public void visitObjectReference(ObjectReference objectReference) {
        if (objectReference.getVariableIdentifier().equals(IdTable.getInstance().getSelfSymbol())) {
            this.variables.push("this");
        } else {
            this.variables.push(this.nameGen.getJavaNameForVariable(objectReference.getVariableIdentifier()));
        }
    }

    @Override
    public void visitStringConstant(StringConst stringConst) {
        final String varName = this.nameGen.getFreshVariableName();
        this.writer.write("CoolString " + varName + " = new CoolString(\"" + stringConst.getValue().toString()
                + "\");\n");
        this.variables.push(varName);
    }

    @Override
    public void visitAdditionPostorder(Addition addition) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        writer.write("CoolInt " + resultVariable + " = new CoolInt(" + lhsVariable + ".getValue() + " + rhsVariable
                + ".getValue());");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitBooleanNegationPostorder(BooleanNegation booleanNegation) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String argVariable = this.variables.pop();
        writer.write("CoolBool " + resultVariable + " = new CoolBool(!" + argVariable + ");");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitDivisionPostorder(Division division) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        writer.write("CoolInt " + resultVariable + " = new CoolInt(" + lhsVariable + ".getValue() / " + rhsVariable
                + ".getValue());");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitEqualityPostorder(Equality equality) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        writer.write("CoolBool " + resultVariable + " = (" + lhsVariable + " == " + rhsVariable + ");");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitIsVoidPostorder(IsVoid isVoid) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String argVariable = this.variables.pop();
        writer.write("CoolBool " + resultVariable + " = (" + argVariable + " == null);");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitLessThanPostorder(LessThan lessThan) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        writer.write("CoolBool " + resultVariable + " = new CoolBool(" + lhsVariable + ".getValue() < " + rhsVariable
                + ".getValue());");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitLessThanOrEqualsPostorder(LessThanOrEquals lessThanOrEquals) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        writer.write("CoolBool " + resultVariable + " = new CoolBool(" + lhsVariable + ".getValue() <= " + rhsVariable
                + ".getValue());");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitMultiplicationPostorder(Multiplication multiplication) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        writer.write("CoolInt " + resultVariable + " = new CoolInt(" + lhsVariable + ".getValue() * " + rhsVariable
                + ".getValue());");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitSubtractionPostorder(Subtraction subtraction) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String rhsVariable = this.variables.pop();
        final String lhsVariable = this.variables.pop();
        writer.write("CoolInt " + resultVariable + " = new CoolInt(" + lhsVariable + ".getValue() - " + rhsVariable
                + ".getValue());");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitArithmeticNegationPostOrder(ArithmeticNegation arithmeticNegation) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        final String argVariable = this.variables.pop();
        writer.write("CoolInt " + resultVariable + " = new CoolInt(-" + argVariable + ".getValue());");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitAssignPostorder(Assign assign) {
        final String expressionVariable = this.variables.pop();
        final String assigneeVariable = this.nameGen.getJavaNameForVariable(assign.getVariableIdentifier());
        writer.write(assigneeVariable + " = " + expressionVariable + ";\n");
        this.variables.push(assigneeVariable);
    }

    @Override
    public void visitBlockExpressionsInorder(BlockExpressions expressions) {
        this.variables.pop();
    }

    @Override
    public void visitTypecaseInorder(Typecase typecase) {
        final String resultVariable = this.nameGen.getFreshVariableName();
        writer.write(this.nameGen.getJavaNameForClass(typecase.getType()) + " " + resultVariable + ";\n");
        this.variables.push(resultVariable);
        writer.write("{\n");
    }

    @Override
    public void visitTypecasePostorder(Typecase typecase) {
        writer.write("}\n");
        final String resultVariable = this.variables.pop();
        // Pop the expression variable
        this.variables.pop();
        this.variables.push(resultVariable);
    }

    @Override
    public void visitCasePreorder(Case caseNode) {
        final String expressionVariable = this.variables.get(this.variables.size() - 2);
        writer.write("if (" + expressionVariable + " instanceof "
                + this.nameGen.getJavaNameForClass(caseNode.getDeclaredType()) + ") {\n");
        writer.write(this.nameGen.getJavaNameForClass(caseNode.getDeclaredType()) + " "
                + this.nameGen.getJavaNameForVariable(caseNode.getVariableIdentifier()) + " = ("
                + this.nameGen.getJavaNameForClass(caseNode.getDeclaredType()) + ")" + expressionVariable + ";\n");
    }

    @Override
    public void visitCasePostorder(Case caseNode) {
        final String expressionVariable = this.variables.pop();
        final String resultVariable = this.variables.peek();
        writer.write(resultVariable + " = " + expressionVariable + ";\n");
        writer.write("}\n");
    }

    @Override
    public void visitIfPreorderOne(If ifNode) {
        final String conditionVariable = this.variables.pop();
        final String resultVariable = this.nameGen.getFreshVariableName();
        writer.write(this.nameGen.getJavaNameForClass(ifNode.getType()) + " " + resultVariable + ";\n");
        this.variables.push(resultVariable);
        writer.write("if (" + conditionVariable + ".getValue()) {\n");
    }

    @Override
    public void visitIfPreorderTwo(If ifNode) {
        final String thenVariable = this.variables.pop();
        final String resultVariable = this.variables.peek();
        writer.write(resultVariable + " = " + thenVariable + ";\n");
        writer.write("} else {\n");
    }

    @Override
    public void visitIfPostorder(If ifNode) {
        final String elseVariable = this.variables.pop();
        final String resultVariable = this.variables.peek();
        writer.write(resultVariable + " = " + elseVariable + ";\n");
        writer.write("}");
    }

    @Override
    public void visitLoopPreorder(Loop loop) {
        writer.write("while(true) {");
    }

    @Override
    public void visitLoopInorder(Loop loop) {
        final String conditionVariable = this.variables.pop();
        writer.write("if(!" + conditionVariable + ".getValue()) { break; }\n");
    }

    @Override
    public void visitLoopPostorder(Loop loop) {
        writer.write("}\n");
        // According to the manual, loops return void, i.e., null
        writer.write(this.variables.peek() + " = null;\n");
    }

    @Override
    public void visitNew(New newNode) {
        final String returnVariable = this.nameGen.getFreshVariableName();
        writer.write(returnVariable + " = new " + this.nameGen.getJavaNameForClass(newNode.getTypeIdentifier())
                + "();\n");
        this.variables.push(returnVariable);
    }

    @Override
    public void visitFunctionCallPostorder(FunctionCall functionCall) {
        final Stack<String> arguments = new Stack<>();
        for (int argIndex = 0; argIndex < functionCall.getArguments().size(); ++argIndex) {
            arguments.push(this.variables.pop());
        }

        final String dispatchVariable = this.variables.pop();
        final String resultVariable = this.nameGen.getFreshVariableName();
        writer.write(this.nameGen.getJavaNameForClass(functionCall.getType()) + " " + resultVariable + " = "
                + dispatchVariable + "(");
        while (!arguments.isEmpty()) {
            writer.write(arguments.pop());
            if (!arguments.isEmpty()) {
                writer.write(", ");
            }
        }
        writer.write(");");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitStaticFunctionCallPostorder(StaticFunctionCall staticFunctionCall) {
        final Stack<String> arguments = new Stack<>();
        for (int argIndex = 0; argIndex < staticFunctionCall.getArguments().size(); ++argIndex) {
            arguments.push(this.variables.pop());
        }

        final String dispatchVariable = this.variables.pop();
        final String resultVariable = this.nameGen.getFreshVariableName();
        writer.write(this.nameGen.getJavaNameForClass(staticFunctionCall.getType()) + " " + resultVariable + " = "
                + dispatchVariable + "(");
        while (!arguments.isEmpty()) {
            writer.write(arguments.pop());
            if (!arguments.isEmpty()) {
                writer.write(", ");
            }
        }
        writer.write(");");
        this.variables.push(resultVariable);
    }

    @Override
    public void visitLetInorder(Let let) {
        final String initializerVariable = this.variables.pop();
        writer.write(this.nameGen.getJavaNameForVariable(let.getVariableIdentifier()) + " = " + initializerVariable
                + ";\n");
    }

}
