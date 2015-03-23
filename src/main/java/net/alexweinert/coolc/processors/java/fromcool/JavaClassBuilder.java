package net.alexweinert.coolc.processors.java.fromcool;

import java.util.Iterator;

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

    private String currentAttribute;

    public JavaClassBuilder(String classId) {
        this.classId = classId;
    }

    public void write(String string) {
        this.stringBuilder.append(string);
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
        final String type = this.namegen.getJavaNameForClass(typeSymbol);
        final String id = this.namegen.getFreshVariableName();
        this.stringBuilder.append(type + " " + id + ";\n");
        return id;
    }

    public void loadBoolean(String target, BoolSymbol value) {
        this.stringBuilder.append("CoolBool " + target + " = new CoolBool(" + value.getString() + ");\n");
    }

    public void loadBoolean(IdSymbol targetSymbol, BoolSymbol value) {
        this.loadBoolean(this.namegen.getJavaNameForVariable(targetSymbol), value);
    }

    public void loadInt(String target, IntSymbol value) {
        this.stringBuilder.append("CoolInt " + target + " = new CoolInt(" + value.getString() + ");\n");
    }

    public void loadInt(IdSymbol targetSymbol, IntSymbol value) {
        this.loadInt(this.namegen.getJavaNameForVariable(targetSymbol), value);
    }

    public void loadString(String target, StringSymbol value) {
        this.stringBuilder.append("CoolString " + target + " = new CoolString(\"" + value.getString() + "\");\n");
    }

    public void loadString(IdSymbol targetSymbol, StringSymbol value) {
        this.loadString(this.namegen.getJavaNameForVariable(targetSymbol), value);
    }

    public void loadVoid(String target) {
        this.stringBuilder.append(target + " = null;\n");
    }

    public void loadVoid(IdSymbol targetSymbol) {
        this.loadVoid(this.namegen.getJavaNameForVariable(targetSymbol));
    }

    public void startMethodDefinition(IdSymbol returnTypeSymbol, IdSymbol idSymbol, Formals formals) {
        final String returnType = this.namegen.getJavaNameForClass(returnTypeSymbol);
        final String id = this.namegen.getJavaNameForMethod(idSymbol);
        this.stringBuilder.append("public " + returnType + " " + id + "(");
        final Iterator<Formal> iterator = formals.iterator();
        while (iterator.hasNext()) {
            final Formal formal = iterator.next();
            final String formalType = this.namegen.getJavaNameForClass(formal.getDeclaredType());
            final String formalId = this.namegen.getJavaNameForClass(formal.getIdentifier());
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

    public void loadVariable(String varName, IdSymbol variableIdentifier) {
        this.stringBuilder.append(varName + " = ");
        if (variableIdentifier.equals(IdTable.getInstance().getSelfSymbol())) {
            this.stringBuilder.append("this");
        } else {
            this.stringBuilder.append(this.namegen.getJavaNameForVariable(variableIdentifier));
        }
        this.stringBuilder.append(";\n");
    }

    public void add(String result, String rhs, String lhs) {
        this.stringBuilder.append(result + " = new CoolInt(" + lhs + ".getValue() + " + rhs + ".getValue());\n");
    }

    public void boolNeg(String result, String arg) {
        this.stringBuilder.append(result + " = new CoolBool(!" + arg + ".getValue());");
    }
}
