package net.alexweinert.coolc.processors.java.variablerenaming;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.alexweinert.coolc.representations.cool.ast.Addition;
import net.alexweinert.coolc.representations.cool.ast.ArgumentExpressions;
import net.alexweinert.coolc.representations.cool.ast.ArithmeticNegation;
import net.alexweinert.coolc.representations.cool.ast.Assign;
import net.alexweinert.coolc.representations.cool.ast.Attribute;
import net.alexweinert.coolc.representations.cool.ast.Block;
import net.alexweinert.coolc.representations.cool.ast.BlockExpressions;
import net.alexweinert.coolc.representations.cool.ast.BoolConst;
import net.alexweinert.coolc.representations.cool.ast.BooleanNegation;
import net.alexweinert.coolc.representations.cool.ast.Case;
import net.alexweinert.coolc.representations.cool.ast.Cases;
import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Classes;
import net.alexweinert.coolc.representations.cool.ast.Division;
import net.alexweinert.coolc.representations.cool.ast.Equality;
import net.alexweinert.coolc.representations.cool.ast.Expression;
import net.alexweinert.coolc.representations.cool.ast.ExpressionBuilder;
import net.alexweinert.coolc.representations.cool.ast.Feature;
import net.alexweinert.coolc.representations.cool.ast.Formal;
import net.alexweinert.coolc.representations.cool.ast.Formals;
import net.alexweinert.coolc.representations.cool.ast.FunctionCall;
import net.alexweinert.coolc.representations.cool.ast.If;
import net.alexweinert.coolc.representations.cool.ast.IntConst;
import net.alexweinert.coolc.representations.cool.ast.IsVoid;
import net.alexweinert.coolc.representations.cool.ast.LessThan;
import net.alexweinert.coolc.representations.cool.ast.LessThanOrEquals;
import net.alexweinert.coolc.representations.cool.ast.Let;
import net.alexweinert.coolc.representations.cool.ast.Loop;
import net.alexweinert.coolc.representations.cool.ast.Method;
import net.alexweinert.coolc.representations.cool.ast.Multiplication;
import net.alexweinert.coolc.representations.cool.ast.New;
import net.alexweinert.coolc.representations.cool.ast.NoExpression;
import net.alexweinert.coolc.representations.cool.ast.ObjectReference;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.ast.StaticFunctionCall;
import net.alexweinert.coolc.representations.cool.ast.StringConst;
import net.alexweinert.coolc.representations.cool.ast.Subtraction;
import net.alexweinert.coolc.representations.cool.ast.Typecase;
import net.alexweinert.coolc.representations.cool.ast.Visitor;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

class VariableRenamingVisitor extends Visitor {

    private final NameGenerator nameGen = new NameGenerator();

    private final List<ClassNode> classes = new LinkedList<>();
    private final List<Feature> features = new LinkedList<>();
    private final Stack<Map<IdSymbol, IdSymbol>> renamings = new Stack<>();

    private final ExpressionBuilder exprBuilder = new ExpressionBuilder();

    public static Program renameVariables(Program input) {
        final VariableRenamingVisitor visitor = new VariableRenamingVisitor();
        input.acceptVisitor(visitor);
        final Classes classes = new Classes(input.getClasses().getFilename(), input.getClasses().getLineNumber(),
                visitor.classes);
        return new Program(input.getFilename(), input.getLineNumber(), classes);
    }

    @Override
    public void visitClassPreorder(ClassNode classNode) {
        final Map<IdSymbol, IdSymbol> newScope = new HashMap<>();
        for (Attribute attribute : classNode.getAttributes()) {
            newScope.put(attribute.getName(), this.nameGen.getNewSymbol());
        }
        this.renamings.push(newScope);
    }

    @Override
    public void visitClassPostorder(ClassNode classNode) {
        this.renamings.pop();
    }

    @Override
    public void visitAttributePostorder(Attribute attribute) {
        final Expression initializer = this.exprBuilder.build();
        final IdSymbol attributeId = this.renamings.peek().get(attribute.getName());
        final IdSymbol attributeType = attribute.getDeclaredType();
        this.features.add(new Attribute(attribute.getFilename(), attribute.getLineNumber(), attributeId, attributeType,
                initializer));
    }

    @Override
    public void visitMethodPreorder(Method method) {
        final Map<IdSymbol, IdSymbol> newRenaming = new HashMap<>(this.renamings.peek());
        for (Formal formal : method.getFormals()) {
            newRenaming.put(formal.getIdentifier(), this.nameGen.getNewSymbol());
        }
        this.renamings.push(newRenaming);
    }

    @Override
    public void visitMethodPostorder(Method method) {
        final List<Formal> formalsList = new LinkedList<>();
        for (Formal formal : method.getFormals()) {
            final IdSymbol formalId = this.renamings.peek().get(formal.getIdentifier());
            formalsList
                    .add(new Formal(formal.getFilename(), formal.getLineNumber(), formalId, formal.getDeclaredType()));
        }
        final Formals formals = new Formals(method.getFormals().getFilename(), method.getFormals().getLineNumber(),
                formalsList);
        final Expression expression = this.exprBuilder.build();
        this.features.add(new Method(method.getFilename(), method.getLineNumber(), method.getName(), formals, method
                .getReturnType(), expression));
        this.renamings.pop();
    }

    @Override
    public void visitAdditionPostorder(Addition addition) {
        this.exprBuilder.addition(addition.getFilename(), addition.getLineNumber());
    }

    @Override
    public void visitArgumentExpressionsPreorder(ArgumentExpressions expressions) {
        this.exprBuilder.startArgumentExpressions();
    }

    @Override
    public void visitArgumentExpressionsInorder(ArgumentExpressions expressions) {
        this.exprBuilder.makeArgumentExpression();
    }

    @Override
    public void visitArgumentExpressionsPostorder(ArgumentExpressions expressions) {
        if (expressions.size() > 0) {
            this.exprBuilder.makeArgumentExpression();
        }
    }

    @Override
    public void visitArithmeticNegationPostOrder(ArithmeticNegation arithmeticNegation) {
        this.exprBuilder.arithmeticNegation(arithmeticNegation.getFilename(), arithmeticNegation.getLineNumber());
    }

    @Override
    public void visitAssignPostorder(Assign assign) {
        final IdSymbol lhs = this.renamings.peek().get(assign.getVariableIdentifier());
        this.exprBuilder.assign(assign.getFilename(), assign.getLineNumber(), lhs);
    }

    @Override
    public void visitBlockPostorder(Block block) {
        this.exprBuilder.block(block.getFilename(), block.getLineNumber());
    }

    @Override
    public void visitBlockExpressionsPreorder(BlockExpressions expressions) {
        this.exprBuilder.startBlockExpressions();
    }

    @Override
    public void visitBlockExpressionsInorder(BlockExpressions expressions) {
        this.exprBuilder.makeBlockExpression();
    }

    @Override
    public void visitBlockExpressionsPostorder(BlockExpressions expressions) {
        if (expressions.size() > 0) {
            this.exprBuilder.makeBlockExpression();
        }
    }

    @Override
    public void visitBoolConst(BoolConst boolConst) {
        this.exprBuilder.boolConst(boolConst.getFilename(), boolConst.getLineNumber(), boolConst.getValue());
    }

    @Override
    public void visitBooleanNegationPostorder(BooleanNegation booleanNegation) {
        this.exprBuilder.booleanNegation(booleanNegation.getFilename(), booleanNegation.getLineNumber());
    }

    @Override
    public void visitCasePreorder(Case caseNode) {
        final Map<IdSymbol, IdSymbol> newRenaming = new HashMap<>(this.renamings.peek());
        newRenaming.put(caseNode.getVariableIdentifier(), this.nameGen.getNewSymbol());
        this.renamings.push(newRenaming);
    }

    @Override
    public void visitCasePostorder(Case caseNode) {
        final IdSymbol variableId = this.renamings.peek().get(caseNode.getVariableIdentifier());
        final IdSymbol variableType = caseNode.getDeclaredType();
        this.exprBuilder.makeCase(caseNode.getFilename(), caseNode.getLineNumber(), variableId, variableType);
    }

    @Override
    public void visitCasesPreorder(Cases cases) {
        this.exprBuilder.startCases();
    }

    @Override
    public void visitDivisionPostorder(Division division) {
        this.exprBuilder.division(division.getFilename(), division.getLineNumber());
    }

    @Override
    public void visitEqualityPostorder(Equality equality) {
        this.exprBuilder.equality(equality.getFilename(), equality.getLineNumber());
    }

    @Override
    public void visitFunctionCallPostorder(FunctionCall functionCall) {
        this.exprBuilder.functionCall(functionCall.getFilename(), functionCall.getLineNumber(),
                functionCall.getFunctionIdentifier());
    }

    @Override
    public void visitIfPostorder(If ifNode) {
        this.exprBuilder.makeIf(ifNode.getFilename(), ifNode.getLineNumber());
    }

    @Override
    public void visitIntConst(IntConst intConst) {
        this.exprBuilder.intConst(intConst.getFilename(), intConst.getLineNumber(),
                Integer.parseInt(intConst.getValue().getString()));
    }

    @Override
    public void visitIsVoidPostorder(IsVoid isVoid) {
        this.exprBuilder.isVoid(isVoid.getFilename(), isVoid.getLineNumber());
    }

    @Override
    public void visitLessThanPostorder(LessThan lessThan) {
        this.exprBuilder.lessThan(lessThan.getFilename(), lessThan.getLineNumber());
    }

    @Override
    public void visitLessThanOrEqualsPostorder(LessThanOrEquals lessThanOrEquals) {
        this.exprBuilder.lessThanEquals(lessThanOrEquals.getFilename(), lessThanOrEquals.getLineNumber());
    }

    @Override
    public void visitLetInorder(Let let) {
        final Map<IdSymbol, IdSymbol> newRenaming = new HashMap<>(this.renamings.peek());
        newRenaming.put(let.getVariableIdentifier(), this.nameGen.getNewSymbol());
        this.renamings.push(newRenaming);
    }

    @Override
    public void visitLetPostorder(Let let) {
        final IdSymbol variableId = this.renamings.peek().get(let.getVariableIdentifier());
        this.exprBuilder.let(let.getFilename(), let.getLineNumber(), variableId, let.getDeclaredType());
        this.renamings.pop();
    }

    @Override
    public void visitLoopPostorder(Loop loop) {
        this.exprBuilder.loop(loop.getFilename(), loop.getLineNumber());
    }

    @Override
    public void visitMultiplicationPostorder(Multiplication multiplication) {
        this.exprBuilder.multiplication(multiplication.getFilename(), multiplication.getLineNumber());
    }

    @Override
    public void visitNew(New newNode) {
        this.exprBuilder.makeNew(newNode.getFilename(), newNode.getLineNumber(), newNode.getTypeIdentifier());
    }

    @Override
    public void visitNoExpression(NoExpression noExpression) {
        this.exprBuilder.noExpr(noExpression.getFilename(), noExpression.getLineNumber());
    }

    @Override
    public void visitObjectReference(ObjectReference objectReference) {
        if (objectReference.getVariableIdentifier().equals(IdTable.getInstance().getSelfSymbol())) {
            this.exprBuilder.objectReference(objectReference.getFilename(), objectReference.getLineNumber(), IdTable
                    .getInstance().getSelfSymbol());
        } else {
            final IdSymbol variableId = this.renamings.peek().get(objectReference.getVariableIdentifier());
            this.exprBuilder
                    .objectReference(objectReference.getFilename(), objectReference.getLineNumber(), variableId);
        }
    }

    @Override
    public void visitStaticFunctionCallPostorder(StaticFunctionCall staticFunctionCall) {
        this.exprBuilder.staticFunctionCall(staticFunctionCall.getFilename(), staticFunctionCall.getLineNumber(),
                staticFunctionCall.getStaticType(), staticFunctionCall.getFunctionIdentifier());
    }

    @Override
    public void visitStringConstant(StringConst stringConst) {
        this.exprBuilder.stringConst(stringConst.getFilename(), stringConst.getLineNumber(), stringConst.getValue()
                .getString());
    }

    @Override
    public void visitSubtractionPostorder(Subtraction subtraction) {
        this.exprBuilder.subtraction(subtraction.getFilename(), subtraction.getLineNumber());
    }

    @Override
    public void visitTypecasePostorder(Typecase typecase) {
        this.exprBuilder.typecase(typecase.getFilename(), typecase.getLineNumber());
    }

}
