package net.alexweinert.coolc.processors.java.fromcool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.processors.cool.tohighlevel.CoolBackendBuilder;
import net.alexweinert.coolc.representations.cool.ast.Formal;
import net.alexweinert.coolc.representations.cool.ast.Formals;
import net.alexweinert.coolc.representations.cool.symboltables.BoolSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;
import net.alexweinert.coolc.representations.cool.symboltables.IntSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.StringSymbol;
import net.alexweinert.coolc.representations.java.JavaClass;
import net.alexweinert.coolc.representations.java.JavaProgram;

class JavaClassBuilder implements CoolBackendBuilder<JavaClass, JavaProgram> {
    private final String classId;
    private final StringBuilder stringBuilder = new StringBuilder();
    private final NameGenerator namegen = new NameGenerator();
    private Stack<String> typecaseControlVariables = new Stack<>();

    private String currentAttribute;

    public JavaClassBuilder(IdSymbol classIdSymbol) {
        this.classId = this.namegen.getJavaNameForClass(classIdSymbol);
    }

    /* (non-Javadoc)
     * 
     * @see
     * net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#beginClass(net.alexweinert.coolc.representations
     * .cool.symboltables.IdSymbol, net.alexweinert.coolc.representations.cool.symboltables.IdSymbol) */
    @Override
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

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#endClass() */
    @Override
    public void endClass() {
        this.stringBuilder.append("}");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#build() */
    @Override
    public JavaClass build() {
        return new JavaClass(this.classId, this.stringBuilder.toString());
    }

    /* (non-Javadoc)
     * 
     * @see
     * net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#beginAttributeDefinition(net.alexweinert.coolc
     * .representations.cool.symboltables.IdSymbol, net.alexweinert.coolc.representations.cool.symboltables.IdSymbol) */
    @Override
    public void beginAttributeDefinition(IdSymbol typeSymbol, IdSymbol idSymbol) {
        final String type = this.namegen.getJavaNameForClass(typeSymbol);
        final String id = this.namegen.getJavaNameForVariable(idSymbol);
        this.stringBuilder.append("protected " + type + " " + id + ";\n");
        this.stringBuilder.append("{");
        this.currentAttribute = id;
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#endAttributeDefinition(java.lang.String) */
    @Override
    public void endAttributeDefinition(String variable) {
        this.stringBuilder.append(this.currentAttribute + " = " + variable + ";\n");
        this.stringBuilder.append("}\n\n");
        this.currentAttribute = null;
    }

    /* (non-Javadoc)
     * 
     * @see
     * net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#declareVariable(net.alexweinert.coolc.representations
     * .cool.symboltables.IdSymbol) */
    @Override
    public String declareVariable(IdSymbol typeSymbol) {
        final String id = this.namegen.getFreshVariableName();
        this.declareVariable(typeSymbol, id);
        return id;
    }

    /* (non-Javadoc)
     * 
     * @see
     * net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#declareVariable(net.alexweinert.coolc.representations
     * .cool.symboltables.IdSymbol, java.lang.String) */
    @Override
    public void declareVariable(IdSymbol typeSymbol, String id) {
        final String type = this.namegen.getJavaNameForClass(typeSymbol);
        this.stringBuilder.append(type + " " + id + " = null;\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#loadVoid(java.lang.String) */
    @Override
    public void loadVoid(String target) {
        this.stringBuilder.append(target + " = null;\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#loadBoolean(java.lang.String,
     * net.alexweinert.coolc.representations.cool.symboltables.BoolSymbol) */
    @Override
    public void loadBoolean(String target, BoolSymbol value) {
        this.stringBuilder.append(target + " = new CoolBool(" + value.getString() + ");\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#loadInt(java.lang.String,
     * net.alexweinert.coolc.representations.cool.symboltables.IntSymbol) */
    @Override
    public void loadInt(String target, IntSymbol value) {
        this.stringBuilder.append(target + " = new CoolInt(" + value.getString() + ");\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#loadString(java.lang.String,
     * net.alexweinert.coolc.representations.cool.symboltables.StringSymbol) */
    @Override
    public void loadString(String target, StringSymbol value) {
        this.stringBuilder.append(target + " = new CoolString(\"" + value.getString() + "\");\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#loadVariable(java.lang.String,
     * net.alexweinert.coolc.representations.cool.symboltables.IdSymbol) */
    @Override
    public void loadVariable(String varName, IdSymbol variableIdentifier) {
        this.stringBuilder.append(varName + " = ");
        if (variableIdentifier.equals(IdTable.getInstance().getSelfSymbol())) {
            this.stringBuilder.append("this");
        } else {
            this.stringBuilder.append(this.namegen.getJavaNameForVariable(variableIdentifier));
        }
        this.stringBuilder.append(";\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#startMethodDefinition(net.alexweinert.coolc.
     * representations.cool.symboltables.IdSymbol, net.alexweinert.coolc.representations.cool.symboltables.IdSymbol,
     * net.alexweinert.coolc.representations.cool.ast.Formals) */
    @Override
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

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#endMethodDefinition(java.lang.String) */
    @Override
    public void endMethodDefinition(String returnVariable) {
        this.stringBuilder.append("return " + returnVariable + ";\n");
        this.stringBuilder.append("}\n\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#arithNeg(java.lang.String, java.lang.String) */
    @Override
    public void arithNeg(String result, String arg) {
        this.stringBuilder.append(result + " = new CoolInt(-" + arg + ".getValue());\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#add(java.lang.String, java.lang.String,
     * java.lang.String) */
    @Override
    public void add(String result, String lhs, String rhs) {
        this.stringBuilder.append(result + " = new CoolInt(" + lhs + ".getValue() + " + rhs + ".getValue());\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#sub(java.lang.String, java.lang.String,
     * java.lang.String) */
    @Override
    public void sub(String result, String lhs, String rhs) {
        this.stringBuilder.append(result + " = new CoolInt(" + lhs + ".getValue() - " + rhs + ".getValue());\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#mul(java.lang.String, java.lang.String,
     * java.lang.String) */
    @Override
    public void mul(String result, String lhs, String rhs) {
        this.stringBuilder.append(result + " = new CoolInt(" + lhs + ".getValue() * " + rhs + ".getValue());\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#div(java.lang.String, java.lang.String,
     * java.lang.String) */
    @Override
    public void div(String result, String lhs, String rhs) {
        this.stringBuilder.append(result + " = new CoolInt(" + lhs + ".getValue() / " + rhs + ".getValue());\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#lt(java.lang.String, java.lang.String,
     * java.lang.String) */
    @Override
    public void lt(String result, String lhs, String rhs) {
        this.stringBuilder.append(result + " = new CoolBool(" + lhs + ".getValue() < " + rhs + ".getValue());\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#lte(java.lang.String, java.lang.String,
     * java.lang.String) */
    @Override
    public void lte(String result, String lhs, String rhs) {
        this.stringBuilder.append(result + " = new CoolBool(" + lhs + ".getValue() <= " + rhs + ".getValue());\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#eq(java.lang.String, java.lang.String,
     * java.lang.String) */
    @Override
    public void eq(String result, String lhs, String rhs) {
        final String bothNull = "(" + lhs + " == null && " + rhs + " == null)";
        final String lhsNotNull = "(" + lhs + "!= null)";
        final String lhsEqualRhs = "(" + lhs + ".equals(" + rhs + "))";
        final String condition = bothNull + " || (" + lhsNotNull + " && " + lhsEqualRhs + ")";
        this.stringBuilder.append(result + " = new CoolBool(" + condition + ");\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#isVoid(java.lang.String, java.lang.String) */
    @Override
    public void isVoid(String result, String arg) {
        this.stringBuilder.append(result + " = new CoolBool(" + arg + " == null);\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#boolNeg(java.lang.String, java.lang.String) */
    @Override
    public void boolNeg(String result, String arg) {
        this.stringBuilder.append(result + " = new CoolBool(!" + arg + ".getValue());\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#assign(java.lang.String, java.lang.String) */
    @Override
    public void assign(String result, String arg) {
        this.stringBuilder.append(result + " = " + arg + ";\n");
    }

    /* (non-Javadoc)
     * 
     * @see
     * net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#toVariable(net.alexweinert.coolc.representations
     * .cool.symboltables.IdSymbol) */
    @Override
    public String toVariable(IdSymbol arg) {
        return this.namegen.getJavaNameForVariable(arg);
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#New(java.lang.String,
     * net.alexweinert.coolc.representations.cool.symboltables.IdSymbol) */
    @Override
    public void New(String returnVariable, IdSymbol typeSymbol) {
        final String type = this.namegen.getJavaNameForClass(typeSymbol);
        this.stringBuilder.append(returnVariable + " = new " + type + "();\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#beginIf() */
    @Override
    public void beginIf() {}

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#beginThen(java.lang.String) */
    @Override
    public void beginThen(String conditionVariable) {
        this.stringBuilder.append("if (" + conditionVariable + ".getValue()) {\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#beginElse() */
    @Override
    public void beginElse() {
        this.stringBuilder.append("} else {\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#endIf() */
    @Override
    public void endIf() {
        this.stringBuilder.append("}\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#beginLoop() */
    @Override
    public void beginLoop() {
        this.stringBuilder.append("while(true) {\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#endLoopCondition(java.lang.String) */
    @Override
    public void endLoopCondition(String conditionVariable) {
        this.stringBuilder.append("if(!" + conditionVariable + ".getValue()) { break; }\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#endLoop() */
    @Override
    public void endLoop() {
        this.stringBuilder.append("}\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#functionCall(java.lang.String,
     * java.lang.String, net.alexweinert.coolc.representations.cool.symboltables.IdSymbol, java.util.List) */
    @Override
    public void functionCall(String resultVariable, String dispatchVariable, String dispatchVariableType,
            IdSymbol functionIdentifier, IdSymbol returnType, List<String> arguments, List<String> argumentTypes) {
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

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#staticFunctionCall(java.lang.String,
     * java.lang.String, net.alexweinert.coolc.representations.cool.symboltables.IdSymbol,
     * net.alexweinert.coolc.representations.cool.symboltables.IdSymbol, java.util.List) */
    @Override
    public void staticFunctionCall(String resultVariable, String dispatchVariable, IdSymbol functionIdentifier,
            IdSymbol staticType, IdSymbol returnType, List<String> arguments, List<String> argumentTypes) {
        // TODO: Actually perform a static function call
        this.functionCall(resultVariable, dispatchVariable, staticType.toString(), functionIdentifier, returnType,
                arguments, argumentTypes);
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#beginTypecase(java.lang.String) */
    @Override
    public void beginTypecase(String expressionVariable) {
        final String controlVariable = this.namegen.getFreshVariableName();
        this.stringBuilder.append("boolean " + controlVariable + " = false;\n");
        this.typecaseControlVariables.push(controlVariable);
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#beginCase(java.lang.String,
     * net.alexweinert.coolc.representations.cool.symboltables.IdSymbol,
     * net.alexweinert.coolc.representations.cool.symboltables.IdSymbol) */
    @Override
    public void beginCase(String expressionVariable, IdSymbol typeSymbol, IdSymbol idSymbol) {
        final String controlVariable = this.typecaseControlVariables.peek();

        final String type = this.namegen.getJavaNameForClass(typeSymbol);
        final String id = this.namegen.getJavaNameForVariable(idSymbol);

        this.stringBuilder.append("if (!" + controlVariable + " && " + expressionVariable + " instanceof " + type
                + ") {\n");
        this.declareVariable(typeSymbol, id);
        this.assign(id, "(" + type + ")" + expressionVariable);
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#endCase() */
    @Override
    public void endCase() {
        final String controlVariable = this.typecaseControlVariables.peek();
        this.stringBuilder.append(controlVariable + " = true;\n");
        this.stringBuilder.append("}\n");
    }

    /* (non-Javadoc)
     * 
     * @see net.alexweinert.coolc.processors.java.fromcool.FromCoolBuilder#endTypecase() */
    @Override
    public void endTypecase() {
        this.typecaseControlVariables.pop();
    }

    @Override
    public Collection<JavaClass> buildBasicClasses() throws ProcessorException {
        final Collection<JavaClass> returnValue = new HashSet<>();
        try {
            returnValue.add(this.copyResource("CoolObject"));
            returnValue.add(this.copyResource("CoolBool"));
            returnValue.add(this.copyResource("CoolInt"));
            returnValue.add(this.copyResource("CoolString"));
            returnValue.add(this.copyResource("CoolIO"));
        } catch (IOException e) {
            throw new ProcessorException(e);
        }
        return returnValue;
    }

    private JavaClass copyResource(String fileName) throws IOException {
        BufferedReader sourceFileReader = new BufferedReader(new FileReader(new File(this.getClass().getClassLoader()
                .getResource(fileName + ".java").getFile())));
        final StringBuilder builder = new StringBuilder();
        String currentLine = sourceFileReader.readLine();

        while (currentLine != null) {
            builder.append(currentLine + "\n");
            currentLine = sourceFileReader.readLine();
        }
        sourceFileReader.close();
        return new JavaClass(fileName, builder.toString());
    }

    @Override
    public JavaProgram buildProgram(List<JavaClass> classes) {
        return new JavaProgram(classes);
    }
}
