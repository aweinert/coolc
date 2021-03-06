package net.alexweinert.coolc.representations.cool.ast;

import java.util.Arrays;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;
import net.alexweinert.coolc.representations.cool.symboltables.IntTable;
import net.alexweinert.coolc.representations.cool.symboltables.StringTable;

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

    public Program program(ClassNode... classes) {
        return new Program(this.filename, this.lineNumber, new Classes(this.filename, this.lineNumber,
                Arrays.asList(classes)));
    }

    public ClassNode classNode(String identifier, String parent, Feature... features) {
        final IdSymbol identifierSymbol = IdTable.getInstance().addString(identifier);
        final IdSymbol parentSymbol = IdTable.getInstance().addString(parent);
        return new ClassNode(this.filename, this.lineNumber, identifierSymbol, parentSymbol, new Features(
                this.filename, this.lineNumber, Arrays.asList(features)));
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

    public IntConst intConst(int value) {
        return new IntConst(this.filename, this.lineNumber, IntTable.getInstance().addInt(value));
    }

    public StringConst stringConst(String value) {
        return new StringConst(this.filename, this.lineNumber, StringTable.getInstance().addString(value));
    }

    public BoolConst boolConst(boolean value) {
        return new BoolConst(this.filename, this.lineNumber, value);
    }

    public ObjectReference varRef(String varName) {
        return new ObjectReference(this.filename, this.lineNumber, IdTable.getInstance().addString(varName));
    }

    public Addition add(Expression lhs, Expression rhs) {
        return new Addition(this.filename, this.lineNumber, lhs, rhs);
    }

    public Subtraction sub(Expression lhs, Expression rhs) {
        return new Subtraction(this.filename, this.lineNumber, lhs, rhs);
    }

    public Multiplication mult(Expression lhs, Expression rhs) {
        return new Multiplication(this.filename, this.lineNumber, lhs, rhs);
    }

    public Division div(Expression lhs, Expression rhs) {
        return new Division(this.filename, this.lineNumber, lhs, rhs);
    }

    public LessThan lt(Expression lhs, Expression rhs) {
        return new LessThan(this.filename, this.lineNumber, lhs, rhs);
    }

    public LessThanOrEquals le(Expression lhs, Expression rhs) {
        return new LessThanOrEquals(this.filename, this.lineNumber, lhs, rhs);
    }

    public Assign assignment(String variable, Expression expr) {
        return new Assign(this.filename, this.lineNumber, IdTable.getInstance().addString(variable), expr);
    }

    public If ifStatement(Expression condition, Expression thenBranch, Expression elseBranch) {
        return new If(this.filename, this.lineNumber, condition, thenBranch, elseBranch);
    }

    public Loop loop(Expression cond, Expression body) {
        return new Loop(this.filename, this.lineNumber, cond, body);
    }

    public Block block(Expression... expressions) {
        return new Block(this.filename, this.lineNumber, new BlockExpressions(this.filename, this.lineNumber,
                Arrays.asList(expressions)));
    }

    public IsVoid isVoid(Expression expr) {
        return new IsVoid(this.filename, this.lineNumber, expr);
    }

    public Let let(String variable, String type, Expression init, Expression expr) {
        final IdSymbol variableSymbol = IdTable.getInstance().addString(variable);
        final IdSymbol typeSymbol = IdTable.getInstance().addString(type);
        return new Let(this.filename, this.lineNumber, variableSymbol, typeSymbol, init, expr);
    }

    public FunctionCall call(Expression calleeObject, String functionId, Expression... args) {
        final IdSymbol functionIdSymbol = IdTable.getInstance().addString(functionId);
        return new FunctionCall(this.filename, this.lineNumber, calleeObject, functionIdSymbol,
                new ArgumentExpressions(this.filename, this.lineNumber, Arrays.asList(args)));
    }

    public StaticFunctionCall staticCall(Expression calleeObject, String calleeType, String functionId,
            Expression... args) {
        final IdSymbol calleeTypeSymbol = IdTable.getInstance().addString(calleeType);
        final IdSymbol functionIdSymbol = IdTable.getInstance().addString(functionId);
        return new StaticFunctionCall(this.filename, this.lineNumber, calleeObject, calleeTypeSymbol, functionIdSymbol,
                new ArgumentExpressions(this.filename, this.lineNumber, Arrays.asList(args)));
    }

    public Expression newNode(String type) {
        final IdSymbol typeSymbol = IdTable.getInstance().addString(type);
        return new New(this.filename, this.lineNumber, typeSymbol);
    }
}
