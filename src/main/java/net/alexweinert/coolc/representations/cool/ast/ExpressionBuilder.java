package net.alexweinert.coolc.representations.cool.ast;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IntTable;
import net.alexweinert.coolc.representations.cool.symboltables.StringTable;

public class ExpressionBuilder {
    private final Stack<Expression> expressions = new Stack<>();
    private final Stack<List<Expression>> blockExpressions = new Stack<>();
    private final Stack<List<Expression>> argumentExpressions = new Stack<>();
    private final Stack<List<Case>> cases = new Stack<>();

    public Expression build() {
        assert this.expressions.size() == 1;
        return this.expressions.pop();
    }

    public void addition(String filename, int lineNumber) {
        final Expression rhsArg = this.expressions.pop();
        final Expression lhsArg = this.expressions.pop();

        this.expressions.push(new Addition(filename, lineNumber, lhsArg, rhsArg));
    }

    public void startArgumentExpressions() {
        this.argumentExpressions.push(new LinkedList<Expression>());
    }

    public void makeArgumentExpression() {
        this.argumentExpressions.peek().add(this.expressions.pop());
    }

    public void arithmeticNegation(String filename, int lineNumber) {
        final Expression arg = this.expressions.pop();
        this.expressions.push(new ArithmeticNegation(filename, lineNumber, arg));
    }

    public void assign(String filename, int lineNumber, IdSymbol lhs) {
        final Expression rhs = this.expressions.pop();
        this.expressions.push(new Assign(filename, lineNumber, lhs, rhs));
    }

    public void block(String filename, int lineNumber) {
        final List<Expression> expressionsList = this.blockExpressions.pop();
        final BlockExpressions expressions = new BlockExpressions(filename, lineNumber, expressionsList);
        this.expressions.push(new Block(filename, lineNumber, expressions));
    }

    public void startBlockExpressions() {
        this.blockExpressions.push(new LinkedList<Expression>());
    }

    public void makeBlockExpression() {
        this.blockExpressions.peek().add(this.expressions.pop());
    }

    public void boolConst(String filename, int lineNumber, boolean value) {
        this.expressions.push(new BoolConst(filename, lineNumber, value));
    }

    public void booleanNegation(String filename, int lineNumber) {
        final Expression arg = this.expressions.pop();
        this.expressions.push(new BooleanNegation(filename, lineNumber, arg));
    }

    public void makeCase(String filename, int lineNumber, IdSymbol varId, IdSymbol typeId) {
        final Expression arg = this.expressions.pop();
        this.cases.peek().add(new Case(filename, lineNumber, varId, typeId, arg));
    }

    public void division(String filename, int lineNumber) {
        final Expression rhsArg = this.expressions.pop();
        final Expression lhsArg = this.expressions.pop();

        this.expressions.push(new Division(filename, lineNumber, lhsArg, rhsArg));
    }

    public void equality(String filename, int lineNumber) {
        final Expression rhsArg = this.expressions.pop();
        final Expression lhsArg = this.expressions.pop();

        this.expressions.push(new Equality(filename, lineNumber, lhsArg, rhsArg));
    }

    public void functionCall(String filename, int lineNumber, IdSymbol functionId) {
        final Expression callExpression = this.expressions.pop();
        final List<Expression> argumentList = this.argumentExpressions.pop();
        final ArgumentExpressions arguments = new ArgumentExpressions(filename, lineNumber, argumentList);
        this.expressions.push(new FunctionCall(filename, lineNumber, callExpression, functionId, arguments));
    }

    public void makeIf(String filename, int lineNumber) {
        final Expression elseBranch = this.expressions.pop();
        final Expression thenBranch = this.expressions.pop();
        final Expression condition = this.expressions.pop();

        this.expressions.push(new If(filename, lineNumber, condition, thenBranch, elseBranch));
    }

    public void intConst(String filename, int lineNumber, int value) {
        this.expressions.push(new IntConst(filename, lineNumber, IntTable.getInstance().addInt(value)));
    }

    public void isVoid(String filename, int lineNumber) {
        final Expression arg = this.expressions.pop();
        this.expressions.push(new IsVoid(filename, lineNumber, arg));
    }

    public void lessThan(String filename, int lineNumber) {
        final Expression rhs = this.expressions.pop();
        final Expression lhs = this.expressions.pop();
        this.expressions.push(new LessThan(filename, lineNumber, rhs, lhs));
    }

    public void lessThanEquals(String filename, int lineNumber) {
        final Expression rhs = this.expressions.pop();
        final Expression lhs = this.expressions.pop();
        this.expressions.push(new LessThanOrEquals(filename, lineNumber, rhs, lhs));
    }

    public void let(String filename, int lineNumber, IdSymbol varId, IdSymbol type) {
        final Expression arg = this.expressions.pop();
        final Expression init = this.expressions.pop();
        this.expressions.push(new Let(filename, lineNumber, varId, type, init, arg));
    }

    public void loop(String filename, int lineNumber) {
        final Expression body = this.expressions.pop();
        final Expression cond = this.expressions.pop();
        this.expressions.push(new Loop(filename, lineNumber, cond, body));
    }

    public void multiplication(String filename, int lineNumber) {
        final Expression rhs = this.expressions.pop();
        final Expression lhs = this.expressions.pop();
        this.expressions.push(new Multiplication(filename, lineNumber, lhs, rhs));
    }

    public void makeNew(String filename, int lineNumber, IdSymbol type) {
        this.expressions.push(new New(filename, lineNumber, type));
    }

    public void noExpr(String filename, int lineNumber) {
        this.expressions.push(new NoExpression(filename, lineNumber));
    }

    public void objectReference(String filename, int lineNumber, IdSymbol objectId) {
        this.expressions.push(new ObjectReference(filename, lineNumber, objectId));
    }

    public void staticFunctionCall(String filename, int lineNumber, IdSymbol staticType, IdSymbol functionId) {
        final Expression callExpression = this.expressions.pop();
        final List<Expression> argumentList = this.argumentExpressions.pop();
        final ArgumentExpressions arguments = new ArgumentExpressions(filename, lineNumber, argumentList);
        this.expressions.push(new StaticFunctionCall(filename, lineNumber, callExpression, staticType, functionId,
                arguments));
    }

    public void stringConst(String filename, int lineNumber, String value) {
        this.expressions.push(new StringConst(filename, lineNumber, StringTable.getInstance().addString(value)));
    }

    public void subtraction(String filename, int lineNumber) {
        final Expression rhs = this.expressions.pop();
        final Expression lhs = this.expressions.pop();
        this.expressions.push(new Subtraction(filename, lineNumber, lhs, rhs));
    }

    public void typecase(String filename, int lineNumber) {
        final Expression expr = this.expressions.pop();
        final List<Case> casesList = this.cases.pop();
        final Cases cases = new Cases(filename, lineNumber, casesList);
        this.expressions.push(new Typecase(filename, lineNumber, expr, cases));
    }
}
