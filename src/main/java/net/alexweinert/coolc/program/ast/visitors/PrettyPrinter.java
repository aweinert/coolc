package net.alexweinert.coolc.program.ast.visitors;

import net.alexweinert.coolc.program.ast.Addition;
import net.alexweinert.coolc.program.ast.ArgumentExpressions;
import net.alexweinert.coolc.program.ast.ArithmeticNegation;
import net.alexweinert.coolc.program.ast.Assign;
import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.Block;
import net.alexweinert.coolc.program.ast.BlockExpressions;
import net.alexweinert.coolc.program.ast.BoolConst;
import net.alexweinert.coolc.program.ast.BooleanNegation;
import net.alexweinert.coolc.program.ast.Case;
import net.alexweinert.coolc.program.ast.Cases;
import net.alexweinert.coolc.program.ast.ClassNode;
import net.alexweinert.coolc.program.ast.Classes;
import net.alexweinert.coolc.program.ast.Division;
import net.alexweinert.coolc.program.ast.Equality;
import net.alexweinert.coolc.program.ast.Features;
import net.alexweinert.coolc.program.ast.Formal;
import net.alexweinert.coolc.program.ast.Formals;
import net.alexweinert.coolc.program.ast.FunctionCall;
import net.alexweinert.coolc.program.ast.If;
import net.alexweinert.coolc.program.ast.IntConst;
import net.alexweinert.coolc.program.ast.IsVoid;
import net.alexweinert.coolc.program.ast.LessThan;
import net.alexweinert.coolc.program.ast.LessThanOrEquals;
import net.alexweinert.coolc.program.ast.Let;
import net.alexweinert.coolc.program.ast.Loop;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.ast.Multiplication;
import net.alexweinert.coolc.program.ast.New;
import net.alexweinert.coolc.program.ast.NoExpression;
import net.alexweinert.coolc.program.ast.ObjectReference;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.ast.StaticFunctionCall;
import net.alexweinert.coolc.program.ast.StringConst;
import net.alexweinert.coolc.program.ast.Subtraction;
import net.alexweinert.coolc.program.ast.TreeNode;
import net.alexweinert.coolc.program.ast.Typecase;

public class PrettyPrinter extends ASTVisitor {

    final private StringBuilder stringBuilder = new StringBuilder();

    public static String printAst(TreeNode tree) {
        final PrettyPrinter printer = new PrettyPrinter();
        tree.acceptVisitor(printer);
        return printer.stringBuilder.toString();
    }

    @Override
    public void visitAdditionPreorder(Addition addition) {
        stringBuilder.append("(");
    }

    @Override
    public void visitAdditionInorder(Addition addition) {
        stringBuilder.append(" + ");
    }

    @Override
    public void visitAdditionPostorder(Addition addition) {
        stringBuilder.append(")");
    }

    @Override
    public void visitArithmeticNegationPreorder(ArithmeticNegation arithmeticNegation) {
        stringBuilder.append("-(");
    }

    @Override
    public void visitArithmeticNegationPostOrder(ArithmeticNegation arithmeticNegation) {
        stringBuilder.append(")");
    }

    @Override
    public void visitAssignPreorder(Assign assign) {
        stringBuilder.append(assign.getVariableIdentifier());
        stringBuilder.append(" <- ");
    }

    @Override
    public void visitAssignPostorder(Assign assign) {}

    @Override
    public void visitAttributePreorder(Attribute attribute) {
        stringBuilder.append(attribute.getName());
        stringBuilder.append(": ");
        stringBuilder.append(attribute.getDeclaredType());
        stringBuilder.append(" <- ");
    }

    @Override
    public void visitAttributePostorder(Attribute attribute) {}

    @Override
    public void visitBlockPreorder(Block block) {
        stringBuilder.append("{\n");
    }

    @Override
    public void visitBlockPostorder(Block block) {
        stringBuilder.append("}\n");
    }

    @Override
    public void visitBoolConst(BoolConst boolConst) {
        stringBuilder.append(boolConst.getValue().toString());
    }

    @Override
    public void visitBooleanNegationPreorder(BooleanNegation booleanNegation) {
        stringBuilder.append("~(");
    }

    @Override
    public void visitBooleanNegationPostorder(BooleanNegation booleanNegation) {
        stringBuilder.append(")");
    }

    @Override
    public void visitCasePreorder(Case caseNode) {
        stringBuilder.append(caseNode.getVariableIdentifier());
        stringBuilder.append(": ");
        stringBuilder.append(caseNode.getDeclaredType());
        stringBuilder.append(" => ");
    }

    @Override
    public void visitCasePostorder(Case caseNode) {
        stringBuilder.append(";\n");
    }

    @Override
    public void visitCasesPreorder(Cases cases) {}

    @Override
    public void visitCasesInorder(Cases cases) {
        stringBuilder.append("\n");
    }

    @Override
    public void visitCasesPostorder(Cases cases) {}

    @Override
    public void visitClassPreorder(ClassNode classNode) {
        stringBuilder.append("class ");
        stringBuilder.append(classNode.getIdentifier());
        stringBuilder.append(" inherits ");
        stringBuilder.append(classNode.getParent());
        stringBuilder.append(" {\n");
    }

    @Override
    public void visitClassPostorder(ClassNode classNode) {
        stringBuilder.append("}");
    }

    @Override
    public void visitClassesPreorder(Classes classes) {}

    @Override
    public void visitClassesInorder(Classes classes) {
        stringBuilder.append(";\n\n");
    }

    @Override
    public void visitClassesPostorder(Classes classes) {}

    @Override
    public void visitDivisionPreorder(Division division) {
        stringBuilder.append("(");
    }

    @Override
    public void visitDivisionInorder(Division division) {
        stringBuilder.append(" / ");
    }

    @Override
    public void visitDivisionPostorder(Division division) {
        stringBuilder.append(")");
    }

    @Override
    public void visitEqualityPreorder(Equality equality) {
        stringBuilder.append("(");
    }

    @Override
    public void visitEqualityInorder(Equality equality) {
        stringBuilder.append(" = ");
    }

    @Override
    public void visitEqualityPostorder(Equality equality) {
        stringBuilder.append(")");
    }

    @Override
    public void visitArgumentExpressionsInorder(ArgumentExpressions expressions) {
        stringBuilder.append(", ");
    }

    @Override
    public void visitBlockExpressionsInorder(BlockExpressions expressions) {
        stringBuilder.append(";\n");
    }

    @Override
    public void visitBlockExpressionsPostorder(BlockExpressions expressions) {
        stringBuilder.append(";\n");
    }

    @Override
    public void visitFeaturesPreorder(Features features) {}

    @Override
    public void visitFeaturesInorder(Features features) {
        stringBuilder.append(";\n\n");
    }

    @Override
    public void visitFeaturesPostorder(Features features) {
        // Finish the last feature with a ; as well
        stringBuilder.append(";");
    }

    @Override
    public void visitFormal(Formal formal) {
        stringBuilder.append(formal.getIdentifier());
        stringBuilder.append(": ");
        stringBuilder.append(formal.getDeclaredType());
    }

    @Override
    public void visitFormalsPreorder(Formals formals) {}

    @Override
    public void visitFormalsInorder(Formals formals) {
        stringBuilder.append(", ");
    }

    @Override
    public void visitFormalsPostorder(Formals formals) {}

    @Override
    public void visitFunctionCallPreorder(FunctionCall functionCall) {}

    @Override
    public void visitFunctionCallInorder(FunctionCall functionCall) {
        stringBuilder.append(".");
        stringBuilder.append(functionCall.getFunctionIdentifier());
        stringBuilder.append("(");
    }

    @Override
    public void visitFunctionCallPostorder(FunctionCall functionCall) {
        stringBuilder.append(")");
    }

    @Override
    public void visitIfPreorder(If ifNode) {
        stringBuilder.append("if ");
    }

    @Override
    public void visitIfPreorderOne(If ifNode) {
        stringBuilder.append(" then ");
    }

    @Override
    public void visitIfPreorderTwo(If ifNode) {
        stringBuilder.append(" else ");
    }

    @Override
    public void visitIfPostorder(If ifNode) {
        stringBuilder.append(" fi");
    }

    @Override
    public void visitIntConst(IntConst intConst) {
        stringBuilder.append(intConst.getValue());
    }

    @Override
    public void visitIsVoidPreorder(IsVoid isVoid) {
        stringBuilder.append("isvoid ");
    }

    @Override
    public void visitIsVoidPostorder(IsVoid isVoid) {}

    @Override
    public void visitLessThanPreorder(LessThan lessThan) {
        stringBuilder.append("(");
    }

    @Override
    public void visitLessThanInorder(LessThan lessThan) {
        stringBuilder.append(" < ");
    }

    @Override
    public void visitLessThanPostorder(LessThan lessThan) {
        stringBuilder.append(")");
    }

    @Override
    public void visitLessThanOrEqualsPreorder(LessThanOrEquals lessThanOrEquals) {
        stringBuilder.append("(");
    }

    @Override
    public void visitLessThanOrEqualsInorder(LessThanOrEquals lessThanOrEquals) {
        stringBuilder.append(" <= ");
    }

    @Override
    public void visitLessThanOrEqualsPostorder(LessThanOrEquals lessThanOrEquals) {
        stringBuilder.append(")");
    }

    @Override
    public void visitLetPreorder(Let let) {
        stringBuilder.append("let ");
        stringBuilder.append(let.getVariableIdentifier());
        stringBuilder.append(": ");
        stringBuilder.append(let.getDeclaredType());
        stringBuilder.append(" <- ");
    }

    @Override
    public void visitLetInorder(Let let) {
        stringBuilder.append(" in \n");
    }

    @Override
    public void visitLetPostorder(Let let) {}

    @Override
    public void visitLoopPreorder(Loop loop) {
        stringBuilder.append("while ");
    }

    @Override
    public void visitLoopInorder(Loop loop) {
        stringBuilder.append(" loop ");
    }

    @Override
    public void visitLoopPostorder(Loop loop) {
        stringBuilder.append(" pool");
    }

    @Override
    public void visitMethodPreorder(Method method) {
        stringBuilder.append(method.getName());
        stringBuilder.append("(");
    }

    @Override
    public void visitMethodInorder(Method method) {
        stringBuilder.append("): ");
        stringBuilder.append(method.getReturnType());
        stringBuilder.append(" {\n");
    }

    @Override
    public void visitMethodPostorder(Method method) {
        stringBuilder.append("}");
    }

    @Override
    public void visitMultiplicationPreorder(Multiplication multiplication) {
        stringBuilder.append("(");
    }

    @Override
    public void visitMultiplicationInorder(Multiplication multiplication) {
        stringBuilder.append(" * ");
    }

    @Override
    public void visitMultiplicationPostorder(Multiplication multiplication) {
        stringBuilder.append(")");
    }

    @Override
    public void visitNew(New newNode) {
        stringBuilder.append("new ");
        stringBuilder.append(newNode.getTypeIdentifier());
    }

    @Override
    public void visitNoExpression(NoExpression noExpression) {
        stringBuilder.append("no_expr");
    }

    @Override
    public void visitObjectReference(ObjectReference objectReference) {
        stringBuilder.append(objectReference.getVariableIdentifier());
    }

    @Override
    public void visitProgramPreorder(Program program) {}

    @Override
    public void visitProgramPostorder(Program program) {}

    @Override
    public void visitStaticFunctionCallPreorder(StaticFunctionCall staticFunctionCall) {}

    @Override
    public void visitStaticFunctionCallInorder(StaticFunctionCall staticFunctionCall) {
        stringBuilder.append("@");
        stringBuilder.append(staticFunctionCall.getStaticType());
        stringBuilder.append(".");
        stringBuilder.append(staticFunctionCall.getFunctionIdentifier());
        stringBuilder.append("(");
    }

    @Override
    public void visitStaticFunctionCallPostorder(StaticFunctionCall staticFunctionCall) {
        stringBuilder.append(")");
    }

    @Override
    public void visitStringConstant(StringConst stringConst) {
        stringBuilder.append('"');
        stringBuilder.append(stringConst.getValue());
        stringBuilder.append('"');
    }

    @Override
    public void visitSubtractionPreorder(Subtraction subtraction) {
        stringBuilder.append("(");
    }

    @Override
    public void visitSubtractionInorder(Subtraction subtraction) {
        stringBuilder.append(" - ");
    }

    @Override
    public void visitSubtractionPostorder(Subtraction subtraction) {
        stringBuilder.append(")");
    }

    @Override
    public void visitTypecasePreorder(Typecase typecase) {
        stringBuilder.append("case ");
    }

    @Override
    public void visitTypecaseInorder(Typecase typecase) {
        stringBuilder.append(" of ");
    }

    @Override
    public void visitTypecasePostorder(Typecase typecase) {
        stringBuilder.append(" esac");
    }

}
