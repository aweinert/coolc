package net.alexweinert.coolc.processors.cool.selftyperemoval;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import net.alexweinert.coolc.representations.cool.ast.Addition;
import net.alexweinert.coolc.representations.cool.ast.ArgumentExpressions;
import net.alexweinert.coolc.representations.cool.ast.ArithmeticNegation;
import net.alexweinert.coolc.representations.cool.ast.Assign;
import net.alexweinert.coolc.representations.cool.ast.Block;
import net.alexweinert.coolc.representations.cool.ast.BlockExpressions;
import net.alexweinert.coolc.representations.cool.ast.BoolConst;
import net.alexweinert.coolc.representations.cool.ast.BooleanNegation;
import net.alexweinert.coolc.representations.cool.ast.Case;
import net.alexweinert.coolc.representations.cool.ast.Cases;
import net.alexweinert.coolc.representations.cool.ast.Division;
import net.alexweinert.coolc.representations.cool.ast.Equality;
import net.alexweinert.coolc.representations.cool.ast.Expression;
import net.alexweinert.coolc.representations.cool.ast.FunctionCall;
import net.alexweinert.coolc.representations.cool.ast.If;
import net.alexweinert.coolc.representations.cool.ast.IntConst;
import net.alexweinert.coolc.representations.cool.ast.IsVoid;
import net.alexweinert.coolc.representations.cool.ast.LessThan;
import net.alexweinert.coolc.representations.cool.ast.LessThanOrEquals;
import net.alexweinert.coolc.representations.cool.ast.Let;
import net.alexweinert.coolc.representations.cool.ast.Loop;
import net.alexweinert.coolc.representations.cool.ast.Multiplication;
import net.alexweinert.coolc.representations.cool.ast.New;
import net.alexweinert.coolc.representations.cool.ast.NoExpression;
import net.alexweinert.coolc.representations.cool.ast.ObjectReference;
import net.alexweinert.coolc.representations.cool.ast.StaticFunctionCall;
import net.alexweinert.coolc.representations.cool.ast.StringConst;
import net.alexweinert.coolc.representations.cool.ast.Subtraction;
import net.alexweinert.coolc.representations.cool.ast.Typecase;
import net.alexweinert.coolc.representations.cool.ast.Visitor;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

class ExpressionSelfTypeRemover extends Visitor {
    public static Expression removeSelfType(IdSymbol containingClass, Expression expression) {
        final ExpressionSelfTypeRemover remover = new ExpressionSelfTypeRemover(containingClass);
        expression.acceptVisitor(remover);
        return remover.arguments.pop();
    }

    private final IdSymbol classId;

    private final Stack<Expression> arguments = new Stack<>();
    private final Stack<List<Expression>> blockExpressions = new Stack<>();
    private final Stack<List<Expression>> argumentExpressions = new Stack<>();
    private final Stack<List<Case>> cases = new Stack<>();

    public ExpressionSelfTypeRemover(IdSymbol classId) {
        this.classId = classId;
    }

    @Override
    public void visitAdditionPostorder(Addition addition) {
        final Expression rhsArg = arguments.pop();
        final Expression lhsArg = arguments.pop();

        arguments.push(new Addition(addition.getFilename(), addition.getLineNumber(), lhsArg, rhsArg));
    }

    @Override
    public void visitArgumentExpressionsInorder(ArgumentExpressions expressions) {
        this.argumentExpressions.peek().add(this.arguments.pop());
    }

    @Override
    public void visitArgumentExpressionsPostorder(ArgumentExpressions expressions) {
        if (expressions.size() > 0) {
            this.argumentExpressions.peek().add(this.arguments.pop());
        }
    }

    @Override
    public void visitArithmeticNegationPostOrder(ArithmeticNegation arithmeticNegation) {
        final Expression arg = arguments.pop();
        arguments
                .push(new ArithmeticNegation(arithmeticNegation.getFilename(), arithmeticNegation.getLineNumber(), arg));
    }

    @Override
    public void visitAssignPostorder(Assign assign) {
        final Expression argument = arguments.pop();
        arguments.push(new Assign(assign.getFilename(), assign.getLineNumber(), assign.getVariableIdentifier(),
                argument));
    }

    @Override
    public void visitBlockPreorder(Block block) {
        this.blockExpressions.push(new LinkedList<Expression>());
    }

    @Override
    public void visitBlockPostorder(Block block) {
        final List<Expression> expressions = this.blockExpressions.pop();
        this.arguments.push(new Block(block.getFilename(), block.getLineNumber(), new BlockExpressions(block
                .getFilename(), block.getLineNumber(), expressions)));
    }

    @Override
    public void visitBlockExpressionsInorder(BlockExpressions expressions) {
        this.blockExpressions.peek().add(this.arguments.pop());
    }

    @Override
    public void visitBlockExpressionsPostorder(BlockExpressions expressions) {
        if (expressions.size() > 0) {
            this.blockExpressions.peek().add(this.arguments.pop());
        }
    }

    @Override
    public void visitBoolConst(BoolConst boolConst) {
        this.arguments.push(boolConst);
    }

    @Override
    public void visitBooleanNegationPostorder(BooleanNegation booleanNegation) {
        final Expression argument = this.arguments.pop();
        this.arguments.push(new BooleanNegation(booleanNegation.getFilename(), booleanNegation.getLineNumber(),
                argument));
    }

    @Override
    public void visitCasePostorder(Case caseNode) {
        final Expression expr = this.arguments.pop();
        final IdSymbol type = SelfTypeRemover.removeSelfType(caseNode.getDeclaredType(), this.classId);
        this.cases.peek()
                .add(new Case(caseNode.getFilename(), caseNode.getLineNumber(), caseNode.getVariableIdentifier(), type,
                        expr));
    }

    @Override
    public void visitDivisionPostorder(Division division) {
        final Expression rhsExpression = this.arguments.pop();
        final Expression lhsExpression = this.arguments.pop();

        this.arguments
                .push(new Division(division.getFilename(), division.getLineNumber(), lhsExpression, rhsExpression));
    }

    @Override
    public void visitEqualityPostorder(Equality equality) {
        final Expression rhsExpression = this.arguments.pop();
        final Expression lhsExpression = this.arguments.pop();

        this.arguments
                .push(new Equality(equality.getFilename(), equality.getLineNumber(), lhsExpression, rhsExpression));
    }

    @Override
    public void visitFunctionCallInorder(FunctionCall functionCall) {
        this.argumentExpressions.push(new LinkedList<Expression>());
    }

    @Override
    public void visitFunctionCallPostorder(FunctionCall functionCall) {
        final Expression dispatchObject = this.arguments.pop();
        final List<Expression> actualsList = this.argumentExpressions.pop();
        final ArgumentExpressions actuals = new ArgumentExpressions(functionCall.getFilename(),
                functionCall.getLineNumber(), actualsList);
        this.arguments.push(new FunctionCall(functionCall.getFilename(), functionCall.getLineNumber(), dispatchObject,
                functionCall.getFunctionIdentifier(), actuals));
    }

    @Override
    public void visitIfPostorder(If ifNode) {
        final Expression elseBranch = this.arguments.pop();
        final Expression thenBranch = this.arguments.pop();
        final Expression condition = this.arguments.pop();

        this.arguments.push(new If(ifNode.getFilename(), ifNode.getLineNumber(), condition, thenBranch, elseBranch));
    }

    @Override
    public void visitIntConst(IntConst intConst) {
        this.arguments.push(intConst);
    }

    @Override
    public void visitIsVoidPostorder(IsVoid isVoid) {
        final Expression argument = this.arguments.pop();
        this.arguments.push(new IsVoid(isVoid.getFilename(), isVoid.getLineNumber(), argument));
    }

    @Override
    public void visitLessThanPostorder(LessThan lessThan) {
        final Expression rhsExpression = this.arguments.pop();
        final Expression lhsExpression = this.arguments.pop();

        this.arguments
                .push(new LessThan(lessThan.getFilename(), lessThan.getLineNumber(), lhsExpression, rhsExpression));
    }

    @Override
    public void visitLessThanOrEqualsPostorder(LessThanOrEquals lessThanOrEquals) {
        final Expression rhsExpression = this.arguments.pop();
        final Expression lhsExpression = this.arguments.pop();

        this.arguments.push(new LessThanOrEquals(lessThanOrEquals.getFilename(), lessThanOrEquals.getLineNumber(),
                lhsExpression, rhsExpression));
    }

    @Override
    public void visitLetPostorder(Let let) {
        final IdSymbol type = SelfTypeRemover.removeSelfType(let.getDeclaredType(), this.classId);
        final Expression letExpr = this.arguments.pop();
        final Expression initExpr = this.arguments.pop();
        this.arguments.push(new Let(let.getFilename(), let.getLineNumber(), let.getVariableIdentifier(), type,
                initExpr, letExpr));
    }

    @Override
    public void visitLoopPostorder(Loop loop) {
        final Expression body = this.arguments.pop();
        final Expression condition = this.arguments.pop();

        this.arguments.push(new Loop(loop.getFilename(), loop.getLineNumber(), body, condition));
    }

    @Override
    public void visitMultiplicationPostorder(Multiplication multiplication) {
        final Expression rhsArg = this.arguments.pop();
        final Expression lhsArg = this.arguments.pop();

        this.arguments.push(new Multiplication(multiplication.getFilename(), multiplication.getLineNumber(), lhsArg,
                rhsArg));
    }

    @Override
    public void visitNew(New newNode) {
        final IdSymbol type = SelfTypeRemover.removeSelfType(newNode.getTypeIdentifier(), this.classId);
        this.arguments.push(new New(newNode.getFilename(), newNode.getLineNumber(), type));
    }

    @Override
    public void visitNoExpression(NoExpression noExpression) {
        this.arguments.push(noExpression);
    }

    @Override
    public void visitObjectReference(ObjectReference objectReference) {
        this.arguments.push(objectReference);
    }

    @Override
    public void visitStaticFunctionCallPostorder(StaticFunctionCall staticFunctionCall) {
        final Expression dispatchObject = this.arguments.pop();
        final IdSymbol staticType = SelfTypeRemover.removeSelfType(staticFunctionCall.getStaticType(), this.classId);
        final List<Expression> actualsList = this.argumentExpressions.pop();
        final ArgumentExpressions actuals = new ArgumentExpressions(staticFunctionCall.getFilename(),
                staticFunctionCall.getLineNumber(), actualsList);
        this.arguments.push(new StaticFunctionCall(staticFunctionCall.getFilename(),
                staticFunctionCall.getLineNumber(), dispatchObject, staticType, staticFunctionCall
                        .getFunctionIdentifier(), actuals));
    }

    @Override
    public void visitStringConstant(StringConst stringConst) {
        this.arguments.push(stringConst);
    }

    @Override
    public void visitSubtractionPostorder(Subtraction subtraction) {
        final Expression rhsArg = this.arguments.pop();
        final Expression lhsArg = this.arguments.pop();

        this.arguments.push(new Subtraction(subtraction.getFilename(), subtraction.getLineNumber(), lhsArg, rhsArg));
    }

    @Override
    public void visitTypecasePreorder(Typecase typecase) {
        this.cases.push(new LinkedList<Case>());
    }

    @Override
    public void visitTypecasePostorder(Typecase typecase) {
        final Expression expr = this.arguments.pop();
        final List<Case> caseList = this.cases.pop();
        final Cases cases = new Cases(typecase.getFilename(), typecase.getLineNumber(), caseList);

        this.arguments.push(new Typecase(typecase.getFilename(), typecase.getLineNumber(), expr, cases));
    }

}
