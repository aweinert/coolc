package net.alexweinert.coolc.processors.java.fromcool;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import net.alexweinert.coolc.representations.cool.ast.Formal;
import net.alexweinert.coolc.representations.cool.ast.Formals;
import net.alexweinert.coolc.representations.cool.symboltables.BoolSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;
import net.alexweinert.coolc.representations.cool.symboltables.IntSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.StringSymbol;
import net.alexweinert.coolc.representations.java.JavaClass;

class JavaClassBuilder {
    private final String classId;
    private final StringBuilder stringBuilder = new StringBuilder();
    private final NameGenerator namegen = new NameGenerator();
    private Stack<String> typecaseControlVariables = new Stack<>();

    private String currentAttribute;

    public JavaClassBuilder(String classId) {
        this.classId = classId;
    }

    public void beginClass(IdSymbol idSymbol, IdSymbol parentSymbol) {
        final String id = this.namegen.getJavaNameForClass(idSymbol);
        final String parent = this.namegen.getJavaNameForClass(parentSymbol);
        this.stringBuilder.append("public class " + id + " extends " + parent + " {\n");

        if (idSymbol.equals(IdTable.getInstance().getMainSymbol())) {
            this.stringBuilder.append("public static void main(String[] args) {\n");
            this.stringBuilder.append("final CoolMain main = new CoolMain();\n");
            this.stringBuilder.append("main.coolmain();\n");
            this.stringBuilder.append("}\n");
        }
    }

    public void endClass() {
        this.stringBuilder.append("}");
    }

    public JavaClass build() {
        return new JavaClass(this.classId, this.stringBuilder.toString());
    }

    public void beginAttributeDefinition(IdSymbol typeSymbol, IdSymbol idSymbol) {
        final String type = this.namegen.getJavaNameForClass(typeSymbol);
        final String id = this.namegen.getJavaNameForVariable(idSymbol);
        this.stringBuilder.append("protected " + type + " " + id + ";\n");
        this.stringBuilder.append("{");
        this.currentAttribute = id;
    }

    public void endAttributeDefinition(String variable) {
        this.stringBuilder.append(this.currentAttribute + " = " + variable + ";\n");
        this.stringBuilder.append("}\n\n");
        this.currentAttribute = null;
    }

    public String declareVariable(IdSymbol typeSymbol) {
        final String id = this.namegen.getFreshVariableName();
        this.declareVariable(typeSymbol, id);
        return id;
    }

    public void declareVariable(IdSymbol typeSymbol, String id) {
        final String type = this.namegen.getJavaNameForClass(typeSymbol);
        this.stringBuilder.append(type + " " + id + " = null;\n");
    }

    public void loadVoid(String target) {
        this.stringBuilder.append(target + " = null;\n");
    }

    public void loadBoolean(String target, BoolSymbol value) {
        this.stringBuilder.append(target + " = new CoolBool(" + value.getString() + ");\n");
    }

    public void loadInt(String target, IntSymbol value) {
        this.stringBuilder.append(target + " = new CoolInt(" + value.getString() + ");\n");
    }

    public void loadString(String target, StringSymbol value) {
        this.stringBuilder.append(target + " = new CoolString(\"" + value.getString() + "\");\n");
    }

    public void loadVariable(String varName, IdSymbol variableIdentifier) {
        this.stringBuilder.append(varName + " = ");
        if (variableIdentifier.equals(IdTable.getInstance().getSelfSymbol())) {
            this.stringBuilder.append("this");
        } else {
            this.stringBuilder.append(this.namegen.getJavaNameForVariable(variableIdentifier));
        }
        this.stringBuilder.append(";\n");
    }

    public void startMethodDefinition(IdSymbol returnTypeSymbol, IdSymbol idSymbol, Formals formals) {
        final String returnType = this.namegen.getJavaNameForClass(returnTypeSymbol);
        final String id = this.namegen.getJavaNameForMethod(idSymbol);
        this.stringBuilder.append("public " + returnType + " " + id + "(");
        final Iterator<Formal> iterator = formals.iterator();
        while (iterator.hasNext()) {
            final Formal formal = iterator.next();
            final String formalType = this.namegen.getJavaNameForClass(formal.getDeclaredType());
            final String formalId = this.namegen.getJavaNameForVariable(formal.getIdentifier());
            this.stringBuilder.append(formalType + " " + formalId);
            if (iterator.hasNext()) {
                this.stringBuilder.append(", ");
            }
        }
        this.stringBuilder.append(") {\n");
    }

    public void endMethodDefinition(String returnVariable) {
        this.stringBuilder.append("return " + returnVariable + ";\n");
        this.stringBuilder.append("}\n\n");
    }

    public void arithNeg(String result, String arg) {
        this.stringBuilder.append(result + " = new CoolInt(-" + arg + ".getValue());\n");
    }

    public void add(String result, String lhs, String rhs) {
        this.stringBuilder.append(result + " = new CoolInt(" + lhs + ".getValue() + " + rhs + ".getValue());\n");
    }

    public void sub(String result, String lhs, String rhs) {
        this.stringBuilder.append(result + " = new CoolInt(" + lhs + ".getValue() - " + rhs + ".getValue());\n");
    }

    public void mul(String result, String lhs, String rhs) {
        this.stringBuilder.append(result + " = new CoolInt(" + lhs + ".getValue() * " + rhs + ".getValue());\n");
    }

    public void div(String result, String lhs, String rhs) {
        this.stringBuilder.append(result + " = new CoolInt(" + lhs + ".getValue() / " + rhs + ".getValue());\n");
    }

    public void lt(String result, String lhs, String rhs) {
        this.stringBuilder.append(result + " = new CoolBool(" + lhs + ".getValue() < " + rhs + ".getValue());\n");
    }

    public void lte(String result, String lhs, String rhs) {
        this.stringBuilder.append(result + " = new CoolBool(" + lhs + ".getValue() <= " + rhs + ".getValue());\n");
    }

    public void eq(String result, String lhs, String rhs) {
        final String bothNull = "(" + lhs + " == null && " + rhs + " == null)";
        final String lhsNotNull = "(" + lhs + "!= null)";
        final String lhsEqualRhs = "(" + lhs + ".equals(" + rhs + "))";
        final String condition = bothNull + " || (" + lhsNotNull + " && " + lhsEqualRhs + ")";
        this.stringBuilder.append(result + " = new CoolBool(" + condition + ");\n");
    }

    public void isVoid(String result, String arg) {
        this.stringBuilder.append(result + " = new CoolBool(" + arg + " == null);\n");
    }

    public void boolNeg(String result, String arg) {
        this.stringBuilder.append(result + " = new CoolBool(!" + arg + ".getValue());\n");
    }

    public void assign(String result, String arg) {
        this.stringBuilder.append(result + " = " + arg + ";\n");
    }

    public String toVariable(IdSymbol arg) {
        return this.namegen.getJavaNameForVariable(arg);
    }

    public void New(String returnVariable, IdSymbol typeSymbol) {
        final String type = this.namegen.getJavaNameForClass(typeSymbol);
        this.stringBuilder.append(returnVariable + " = new " + type + "();\n");
    }

    public void beginIf() {}

    public void beginThen(String conditionVariable) {
        this.stringBuilder.append("if (" + conditionVariable + ".getValue()) {\n");
    }

    public void beginElse() {
        this.stringBuilder.append("} else {\n");
    }

    public void endIf() {
        this.stringBuilder.append("}\n");
    }

    public void beginLoop() {
        this.stringBuilder.append("while(true) {\n");
    }

    public void endLoopCondition(String conditionVariable) {
        this.stringBuilder.append("if(!" + conditionVariable + ".getValue()) { break; }\n");
    }

    public void endLoop() {
        this.stringBuilder.append("}\n");
    }

    public void functionCall(String resultVariable, String dispatchVariable, IdSymbol functionIdentifier,
            List<String> arguments) {
        final String methodId = this.namegen.getJavaNameForMethod(functionIdentifier);
        this.stringBuilder.append(resultVariable + " = " + dispatchVariable + "." + methodId + "(");
        final Iterator<String> iterator = arguments.iterator();
        while (iterator.hasNext()) {
            this.stringBuilder.append(iterator.next());
            if (iterator.hasNext()) {
                this.stringBuilder.append(", ");
            }
        }
        this.stringBuilder.append(");\n");

    }

    public void staticFunctionCall(String resultVariable, String dispatchVariable, IdSymbol functionIdentifier,
            IdSymbol staticType, List<String> arguments) {
        // TODO: Actually perform a static function call
        this.functionCall(resultVariable, dispatchVariable, functionIdentifier, arguments);
    }

    public void beginTypecase(String expressionVariable) {
        final String controlVariable = this.namegen.getFreshVariableName();
        this.stringBuilder.append("boolean " + controlVariable + " = false;\n");
        this.typecaseControlVariables.push(controlVariable);
    }

    public void beginCase(String expressionVariable, IdSymbol typeSymbol, IdSymbol idSymbol) {
        final String controlVariable = this.typecaseControlVariables.peek();

        final String type = this.namegen.getJavaNameForClass(typeSymbol);
        final String id = this.namegen.getJavaNameForVariable(idSymbol);

        this.stringBuilder.append("if (!" + controlVariable + " && " + expressionVariable + " instanceof " + type
                + ") {\n");
        this.declareVariable(typeSymbol, id);
        this.assign(id, "(" + type + ")" + expressionVariable);
    }

    public void endCase() {
        final String controlVariable = this.typecaseControlVariables.peek();
        this.stringBuilder.append(controlVariable + " = true;\n");
        this.stringBuilder.append("}\n");
    }

    public void endTypecase() {
        this.typecaseControlVariables.pop();
    }
}
