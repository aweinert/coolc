package net.alexweinert.coolc.program.ast;

import java.util.Arrays;

import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

public class ASTFactory {
    private final String filename;

    public ASTFactory() {
        this("");
    }

    public ASTFactory(String filename) {
        this.filename = filename;
    }

    public Program program(Class... classes) {
        return new Program(this.filename, 1, new Classes(this.filename, 1, Arrays.asList(classes)));
    }

    public Class classNode(String identifier, String parent, Feature... features) {
        final IdSymbol identifierSymbol = IdTable.getInstance().addString(identifier);
        final IdSymbol parentSymbol = IdTable.getInstance().addString(parent);
        return new Class(this.filename, 1, identifierSymbol, parentSymbol, new Features(this.filename, 1,
                Arrays.asList(features)));
    }

    public Attribute attribute(String identifier, String type) {
        final Expression noExpression = new NoExpression(this.filename, 1);
        return this.attribute(identifier, type, noExpression);
    }

    public Attribute attribute(String identifier, String type, Expression initializer) {
        final IdSymbol identifierSymbol = IdTable.getInstance().addString(identifier);
        final IdSymbol typeSymbol = IdTable.getInstance().addString(type);

        return new Attribute(this.filename, 1, identifierSymbol, typeSymbol, initializer);
    }
}
