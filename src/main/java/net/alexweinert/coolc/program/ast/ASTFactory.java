package net.alexweinert.coolc.program.ast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

public class ASTFactory {
    private final String filename;
    private int lineNumber;

    public ASTFactory() {
        this("", 1);
    }

    public ASTFactory(String filename, int lineNumber) {
        this.filename = filename;
        this.lineNumber = lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Program program(Class... classes) {
        return new Program(this.filename, this.lineNumber, new Classes(this.filename, this.lineNumber,
                Arrays.asList(classes)));
    }

    public Class classNode(String identifier, String parent, Feature... features) {
        final IdSymbol identifierSymbol = IdTable.getInstance().addString(identifier);
        final IdSymbol parentSymbol = IdTable.getInstance().addString(parent);
        return new Class(this.filename, this.lineNumber, identifierSymbol, parentSymbol, new Features(this.filename,
                this.lineNumber, Arrays.asList(features)));
    }

    public Attribute attribute(String identifier, String type) {
        final Expression noExpression = new NoExpression(this.filename, this.lineNumber);
        return this.attribute(identifier, type, noExpression);
    }

    public Attribute attribute(String identifier, String type, Expression initializer) {
        final IdSymbol identifierSymbol = IdTable.getInstance().addString(identifier);
        final IdSymbol typeSymbol = IdTable.getInstance().addString(type);

        return new Attribute(this.filename, this.lineNumber, identifierSymbol, typeSymbol, initializer);
    }

    public Method method(String identifier, String returnType, Formal... parameterTypes) {
        final Expression noExpression = new NoExpression(this.filename, this.lineNumber);
        return this.method(identifier, returnType, noExpression, parameterTypes);
    }

    public Method method(String identifier, String returnType, Expression body, Formal... formalList) {
        final IdSymbol identifierSymbol = IdTable.getInstance().addString(identifier);
        final IdSymbol returnTypeSymbol = IdTable.getInstance().addString(returnType);

        final Formals formals = new Formals(this.filename, this.lineNumber, Arrays.asList(formalList));

        return new Method(this.filename, this.lineNumber, identifierSymbol, formals, returnTypeSymbol, body);
    }

    public Formal formal(String identifier, String type) {
        final IdSymbol identifierSymbol = IdTable.getInstance().addString(identifier);
        final IdSymbol typeSymbol = IdTable.getInstance().addString(type);

        return new Formal(this.filename, this.lineNumber, identifierSymbol, typeSymbol);
    }
}
