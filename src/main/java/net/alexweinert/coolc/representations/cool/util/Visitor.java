package net.alexweinert.coolc.representations.cool.util;

import net.alexweinert.coolc.representations.cool.expressions.untyped.Addition;
import net.alexweinert.coolc.representations.cool.expressions.untyped.ArgumentExpressions;
import net.alexweinert.coolc.representations.cool.expressions.untyped.ArithmeticNegation;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Assign;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Block;
import net.alexweinert.coolc.representations.cool.expressions.untyped.BlockExpressions;
import net.alexweinert.coolc.representations.cool.expressions.untyped.BoolConst;
import net.alexweinert.coolc.representations.cool.expressions.untyped.BooleanNegation;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Case;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Cases;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Division;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Equality;
import net.alexweinert.coolc.representations.cool.expressions.untyped.FunctionCall;
import net.alexweinert.coolc.representations.cool.expressions.untyped.If;
import net.alexweinert.coolc.representations.cool.expressions.untyped.IntConst;
import net.alexweinert.coolc.representations.cool.expressions.untyped.IsVoid;
import net.alexweinert.coolc.representations.cool.expressions.untyped.LessThan;
import net.alexweinert.coolc.representations.cool.expressions.untyped.LessThanOrEquals;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Let;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Loop;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Multiplication;
import net.alexweinert.coolc.representations.cool.expressions.untyped.New;
import net.alexweinert.coolc.representations.cool.expressions.untyped.NoExpression;
import net.alexweinert.coolc.representations.cool.expressions.untyped.ObjectReference;
import net.alexweinert.coolc.representations.cool.expressions.untyped.StaticFunctionCall;
import net.alexweinert.coolc.representations.cool.expressions.untyped.StringConst;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Subtraction;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Typecase;
import net.alexweinert.coolc.representations.cool.program.parsed.Attribute;
import net.alexweinert.coolc.representations.cool.program.parsed.ClassNode;
import net.alexweinert.coolc.representations.cool.program.parsed.Classes;
import net.alexweinert.coolc.representations.cool.program.parsed.Features;
import net.alexweinert.coolc.representations.cool.program.parsed.Formal;
import net.alexweinert.coolc.representations.cool.program.parsed.Formals;
import net.alexweinert.coolc.representations.cool.program.parsed.Method;
import net.alexweinert.coolc.representations.cool.program.parsed.Program;
import net.alexweinert.coolc.representations.cool.util.*;

public abstract class Visitor {

    public void visitAdditionPreorder(Addition addition) {}

    public void visitAdditionInorder(Addition addition) {}

    public void visitAdditionPostorder(Addition addition) {}

    public void visitArgumentExpressionsPreorder(ArgumentExpressions expressions) {}

    public void visitArgumentExpressionsInorder(ArgumentExpressions expressions) {}

    public void visitArgumentExpressionsPostorder(ArgumentExpressions expressions) {}

    public void visitArithmeticNegationPreorder(ArithmeticNegation arithmeticNegation) {}

    public void visitArithmeticNegationPostOrder(ArithmeticNegation arithmeticNegation) {}

    public void visitAssignPreorder(Assign assign) {}

    public void visitAssignPostorder(Assign assign) {}

    public void visitAttributePreorder(Attribute attribute) {}

    public void visitAttributePostorder(Attribute attribute) {}

    public void visitBlockPreorder(Block block) {}

    public void visitBlockPostorder(Block block) {}

    public void visitBlockExpressionsPreorder(BlockExpressions expressions) {}

    public void visitBlockExpressionsInorder(BlockExpressions expressions) {}

    public void visitBlockExpressionsPostorder(BlockExpressions expressions) {}

    public void visitBoolConst(BoolConst boolConst) {}

    public void visitBooleanNegationPreorder(BooleanNegation booleanNegation) {}

    public void visitBooleanNegationPostorder(BooleanNegation booleanNegation) {}

    public void visitCasePreorder(Case caseNode) {}

    public void visitCasePostorder(Case caseNode) {}

    public void visitCasesPreorder(Cases cases) {}

    public void visitCasesInorder(Cases cases) {}

    public void visitCasesPostorder(Cases cases) {}

    public void visitClassPreorder(ClassNode classNode) {}

    public void visitClassPostorder(ClassNode classNode) {}

    public void visitClassesPreorder(Classes classes) {}

    public void visitClassesInorder(Classes classes) {}

    public void visitClassesPostorder(Classes classes) {}

    public void visitDivisionPreorder(Division division) {}

    public void visitDivisionInorder(Division division) {}

    public void visitDivisionPostorder(Division division) {}

    public void visitEqualityPreorder(Equality equality) {}

    public void visitEqualityInorder(Equality equality) {}

    public void visitEqualityPostorder(Equality equality) {}

    public void visitFeaturesPreorder(Features features) {}

    public void visitFeaturesInorder(Features features) {}

    public void visitFeaturesPostorder(Features features) {}

    public void visitFormal(Formal formal) {}

    public void visitFormalsPreorder(Formals formals) {}

    public void visitFormalsInorder(Formals formals) {}

    public void visitFormalsPostorder(Formals formals) {}

    public void visitFunctionCallPreorder(FunctionCall functionCall) {}

    public void visitFunctionCallInorder(FunctionCall functionCall) {}

    public void visitFunctionCallPostorder(FunctionCall functionCall) {}

    public void visitIfPreorder(If ifNode) {}

    public void visitIfPreorderOne(If ifNode) {}

    public void visitIfPreorderTwo(If ifNode) {}

    public void visitIfPostorder(If ifNode) {}

    public void visitIntConst(IntConst intConst) {}

    public void visitIsVoidPreorder(IsVoid isVoid) {}

    public void visitIsVoidPostorder(IsVoid isVoid) {}

    public void visitLessThanPreorder(LessThan lessThan) {}

    public void visitLessThanInorder(LessThan lessThan) {}

    public void visitLessThanPostorder(LessThan lessThan) {}

    public void visitLessThanOrEqualsPreorder(LessThanOrEquals lessThanOrEquals) {}

    public void visitLessThanOrEqualsInorder(LessThanOrEquals lessThanOrEquals) {}

    public void visitLessThanOrEqualsPostorder(LessThanOrEquals lessThanOrEquals) {}

    public void visitLetPreorder(Let let) {}

    public void visitLetInorder(Let let) {}

    public void visitLetPostorder(Let let) {}

    public void visitLoopPreorder(Loop loop) {}

    public void visitLoopInorder(Loop loop) {}

    public void visitLoopPostorder(Loop loop) {}

    public void visitMethodPreorder(Method method) {}

    public void visitMethodInorder(Method method) {}

    public void visitMethodPostorder(Method method) {}

    public void visitMultiplicationPreorder(Multiplication multiplication) {}

    public void visitMultiplicationInorder(Multiplication multiplication) {}

    public void visitMultiplicationPostorder(Multiplication multiplication) {}

    public void visitNew(New newNode) {}

    public void visitNoExpression(NoExpression noExpression) {}

    public void visitObjectReference(ObjectReference objectReference) {}

    public void visitProgramPreorder(Program program) {}

    public void visitProgramPostorder(Program program) {}

    public void visitStaticFunctionCallPreorder(StaticFunctionCall staticFunctionCall) {}

    public void visitStaticFunctionCallInorder(StaticFunctionCall staticFunctionCall) {}

    public void visitStaticFunctionCallPostorder(StaticFunctionCall staticFunctionCall) {}

    public void visitStringConstant(StringConst stringConst) {}

    public void visitSubtractionPreorder(Subtraction subtraction) {}

    public void visitSubtractionInorder(Subtraction subtraction) {}

    public void visitSubtractionPostorder(Subtraction subtraction) {}

    public void visitTypecasePreorder(Typecase typecase) {}

    public void visitTypecaseInorder(Typecase typecase) {}

    public void visitTypecasePostorder(Typecase typecase) {}
}