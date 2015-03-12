package net.alexweinert.coolc.representations.cool.expressions.typed;

import net.alexweinert.coolc.representations.cool.program.parsed.Attribute;
import net.alexweinert.coolc.representations.cool.program.parsed.ClassNode;
import net.alexweinert.coolc.representations.cool.program.parsed.Classes;
import net.alexweinert.coolc.representations.cool.program.parsed.Features;
import net.alexweinert.coolc.representations.cool.program.parsed.Formal;
import net.alexweinert.coolc.representations.cool.program.parsed.Formals;
import net.alexweinert.coolc.representations.cool.program.parsed.Method;
import net.alexweinert.coolc.representations.cool.program.parsed.Program;
import net.alexweinert.coolc.representations.cool.util.*;

public abstract class TypedExpressionVisitor {

    public void visitAdditionPreorder(TypedAddition addition) {}

    public void visitAdditionInorder(TypedAddition addition) {}

    public void visitAdditionPostorder(TypedAddition addition) {}

    public void visitArgumentExpressionsPreorder(TypedArgumentExpressions expressions) {}

    public void visitArgumentExpressionsInorder(TypedArgumentExpressions expressions) {}

    public void visitArgumentExpressionsPostorder(TypedArgumentExpressions expressions) {}

    public void visitArithmeticNegationPreorder(TypedArithmeticNegation arithmeticNegation) {}

    public void visitArithmeticNegationPostOrder(TypedArithmeticNegation arithmeticNegation) {}

    public void visitAssignPreorder(TypedAssign assign) {}

    public void visitAssignPostorder(TypedAssign assign) {}

    public void visitBlockPreorder(TypedBlock block) {}

    public void visitBlockPostorder(TypedBlock block) {}

    public void visitBlockExpressionsPreorder(TypedBlockExpressions expressions) {}

    public void visitBlockExpressionsInorder(TypedBlockExpressions expressions) {}

    public void visitBlockExpressionsPostorder(TypedBlockExpressions expressions) {}

    public void visitBoolConst(TypedBoolConst boolConst) {}

    public void visitBooleanNegationPreorder(TypedBooleanNegation booleanNegation) {}

    public void visitBooleanNegationPostorder(TypedBooleanNegation booleanNegation) {}

    public void visitCasePreorder(TypedCase caseNode) {}

    public void visitCasePostorder(TypedCase caseNode) {}

    public void visitCasesPreorder(TypedCases cases) {}

    public void visitCasesInorder(TypedCases cases) {}

    public void visitCasesPostorder(TypedCases cases) {}

    public void visitDivisionPreorder(TypedDivision division) {}

    public void visitDivisionInorder(TypedDivision division) {}

    public void visitDivisionPostorder(TypedDivision division) {}

    public void visitEqualityPreorder(TypedEquality equality) {}

    public void visitEqualityInorder(TypedEquality equality) {}

    public void visitEqualityPostorder(TypedEquality equality) {}

    public void visitFunctionCallPreorder(TypedFunctionCall functionCall) {}

    public void visitFunctionCallInorder(TypedFunctionCall functionCall) {}

    public void visitFunctionCallPostorder(TypedFunctionCall functionCall) {}

    public void visitIfPreorder(TypedIf ifNode) {}

    public void visitIfPreorderOne(TypedIf ifNode) {}

    public void visitIfPreorderTwo(TypedIf ifNode) {}

    public void visitIfPostorder(TypedIf ifNode) {}

    public void visitIntConst(TypedIntConst intConst) {}

    public void visitIsVoidPreorder(TypedIsVoid isVoid) {}

    public void visitIsVoidPostorder(TypedIsVoid isVoid) {}

    public void visitLessThanPreorder(TypedLessThan lessThan) {}

    public void visitLessThanInorder(TypedLessThan lessThan) {}

    public void visitLessThanPostorder(TypedLessThan lessThan) {}

    public void visitLessThanOrEqualsPreorder(TypedLessThanOrEquals lessThanOrEquals) {}

    public void visitLessThanOrEqualsInorder(TypedLessThanOrEquals lessThanOrEquals) {}

    public void visitLessThanOrEqualsPostorder(TypedLessThanOrEquals lessThanOrEquals) {}

    public void visitLetPreorder(TypedLet let) {}

    public void visitLetInorder(TypedLet let) {}

    public void visitLetPostorder(TypedLet let) {}

    public void visitLoopPreorder(TypedLoop loop) {}

    public void visitLoopInorder(TypedLoop loop) {}

    public void visitLoopPostorder(TypedLoop loop) {}

    public void visitMultiplicationPreorder(TypedMultiplication multiplication) {}

    public void visitMultiplicationInorder(TypedMultiplication multiplication) {}

    public void visitMultiplicationPostorder(TypedMultiplication multiplication) {}

    public void visitNew(TypedNew newNode) {}

    public void visitNoExpression(TypedNoExpression noExpression) {}

    public void visitObjectReference(TypedObjectReference objectReference) {}

    public void visitStaticFunctionCallPreorder(TypedStaticFunctionCall staticFunctionCall) {}

    public void visitStaticFunctionCallInorder(TypedStaticFunctionCall staticFunctionCall) {}

    public void visitStaticFunctionCallPostorder(TypedStaticFunctionCall staticFunctionCall) {}

    public void visitStringConstant(TypedStringConst stringConst) {}

    public void visitSubtractionPreorder(TypedSubtraction subtraction) {}

    public void visitSubtractionInorder(TypedSubtraction subtraction) {}

    public void visitSubtractionPostorder(TypedSubtraction subtraction) {}

    public void visitTypecasePreorder(TypedTypecase typecase) {}

    public void visitTypecaseInorder(TypedTypecase typecase) {}

    public void visitTypecasePostorder(TypedTypecase typecase) {}
}
