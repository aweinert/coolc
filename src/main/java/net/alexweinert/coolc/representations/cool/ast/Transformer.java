package net.alexweinert.coolc.representations.cool.ast;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Transformer extends Visitor {
    private List<Feature> features;
    private List<ClassNode> classes;
    private Program program;

    private Stack<Expression> expressions = new Stack<>();
    private Stack<List<Expression>> blockExpressions = new Stack<>();
    private Stack<List<Expression>> argumentExpressions = new Stack<>();
    private Stack<List<Case>> cases = new Stack<>();

    public Program transform(final Program program) {
        program.acceptVisitor(this);
        return this.program;
    }

    @Override
    public void visitProgramPreorder(Program program) {
        this.classes = new LinkedList<>();
    }

    @Override
    public void visitProgramPostorder(Program program) {
        final Program newProgram = new Program(program.getFilename(), program.getLineNumber(), new Classes(program
                .getClasses().getFilename(), program.getClasses().getLineNumber(), this.classes));
        this.program = this.transformProgram(newProgram);
    }

    public Program transformProgram(Program program) {
        return program;
    }

    @Override
    public void visitClassPreorder(ClassNode classNode) {
        this.features = new LinkedList<>();
    }

    @Override
    public void visitClassPostorder(ClassNode classNode) {
        final ClassNode newClass = new ClassNode(classNode.getFilename(), classNode.getLineNumber(),
                classNode.getIdentifier(), classNode.getParent(), new Features(classNode.getFeatures().getFilename(),
                        classNode.getFeatures().getLineNumber(), this.features));
        this.classes.add(this.transformClass(newClass));
    }

    public ClassNode transformClass(ClassNode classNode) {
        return classNode;
    }

    @Override
    public void visitAttributePostorder(Attribute attribute) {
        final Attribute newAttribute = new Attribute(attribute.getFilename(), attribute.getLineNumber(),
                attribute.getName(), attribute.getDeclaredType(), this.expressions.pop());
        this.features.add(this.transformAttribute(newAttribute));
    }

    public Feature transformAttribute(Attribute attribute) {
        return attribute;
    }

    @Override
    public void visitMethodPostorder(Method method) {
        final Expression initializer = this.expressions.pop();
        final Method newMethod = new Method(method.getFilename(), method.getLineNumber(), method.getName(),
                method.getFormals(), method.getReturnType(), initializer);
        this.features.add(this.transformMethod(newMethod));
    }

    public Feature transformMethod(Method method) {
        return method;
    }

    @Override
    public void visitBoolConst(BoolConst boolConst) {
        this.expressions.push(this.transformBoolConst(boolConst));
    }

    public Expression transformBoolConst(BoolConst boolConst) {
        return boolConst;
    }

    @Override
    public void visitIntConst(IntConst intConst) {
        this.expressions.push(this.transformIntConst(intConst));
    }

    public Expression transformIntConst(IntConst intConst) {
        return intConst;
    }

    @Override
    public void visitStringConstant(StringConst stringConst) {
        this.expressions.push(this.transformStringConst(stringConst));
    }

    public Expression transformStringConst(StringConst stringConst) {
        return stringConst;
    }

    @Override
    public void visitObjectReference(ObjectReference objectReference) {
        this.expressions.push(this.transformObjectReference(objectReference));
    }

    public Expression transformObjectReference(ObjectReference objectReference) {
        return objectReference;
    }

    @Override
    public void visitAdditionPostorder(Addition addition) {
        final Expression rhs = this.expressions.pop();
        final Expression lhs = this.expressions.pop();
        final Addition newAddition = new Addition(addition.getFilename(), addition.getLineNumber(), lhs, rhs);
        this.expressions.push(this.transformAddition(newAddition));
    }

    public Expression transformAddition(Addition addition) {
        return addition;
    }

    @Override
    public void visitArgumentExpressionsInorder(ArgumentExpressions expressions) {
        this.argumentExpressions.peek().add(this.expressions.pop());
    }

    @Override
    public void visitArgumentExpressionsPostorder(ArgumentExpressions expressions) {
        if (expressions.size() > 0) {
            this.argumentExpressions.peek().add(this.expressions.pop());
        }
    }

    @Override
    public void visitArithmeticNegationPostOrder(ArithmeticNegation arithmeticNegation) {
        final Expression arg = this.expressions.pop();
        final ArithmeticNegation newArithmeticNegation = new ArithmeticNegation(arithmeticNegation.getFilename(),
                arithmeticNegation.getLineNumber(), arg);
        this.expressions.push(this.transformArithmeticNegation(newArithmeticNegation));
    }

    public Expression transformArithmeticNegation(ArithmeticNegation arithmeticNegation) {
        return arithmeticNegation;
    }

    @Override
    public void visitAssignPostorder(Assign assign) {
        final Expression arg = this.expressions.pop();
        final Assign newAssign = new Assign(assign.getFilename(), assign.getLineNumber(),
                assign.getVariableIdentifier(), arg);
        this.expressions.push(this.transformAssign(newAssign));
    }

    public Expression transformAssign(Assign assign) {
        return assign;
    }

    @Override
    public void visitBlockPreorder(Block block) {
        this.blockExpressions.push(new LinkedList<Expression>());
    }

    @Override
    public void visitBlockPostorder(Block block) {
        final List<Expression> expressionsList = this.blockExpressions.pop();
        final BlockExpressions expressions = new BlockExpressions(block.getFilename(), block.getLineNumber(),
                expressionsList);
        final Block newBlock = new Block(block.getFilename(), block.getLineNumber(), expressions);
        this.expressions.push(this.transformBlock(newBlock));
    }

    public Expression transformBlock(Block block) {
        return block;
    }

    @Override
    public void visitBlockExpressionsInorder(BlockExpressions expressions) {
        this.blockExpressions.peek().add(this.expressions.pop());
    }

    @Override
    public void visitBlockExpressionsPostorder(BlockExpressions expressions) {
        if (expressions.size() > 0) {
            this.blockExpressions.peek().add(this.expressions.pop());
        }
    }

    @Override
    public void visitBooleanNegationPostorder(BooleanNegation booleanNegation) {
        final Expression arg = this.expressions.pop();
        final BooleanNegation newBooleanNegation = new BooleanNegation(booleanNegation.getFilename(),
                booleanNegation.getLineNumber(), arg);
        this.expressions.push(this.transformBooleanNegation(newBooleanNegation));
    }

    public Expression transformBooleanNegation(BooleanNegation booleanNegation) {
        return booleanNegation;
    }

    @Override
    public void visitCasePostorder(Case caseNode) {
        final Expression expr = this.expressions.pop();
        final Case newCase = new Case(caseNode.getFilename(), caseNode.getLineNumber(),
                caseNode.getVariableIdentifier(), caseNode.getDeclaredType(), expr);
        this.cases.peek().add(this.transformCase(newCase));
    }

    public Case transformCase(Case caseNode) {
        return caseNode;
    }

    @Override
    public void visitDivisionPostorder(Division division) {
        final Expression rhs = this.expressions.pop();
        final Expression lhs = this.expressions.pop();

        final Division newDivision = new Division(division.getFilename(), division.getLineNumber(), lhs, rhs);
        this.expressions.push(this.transformDivision(newDivision));
    }

    public Expression transformDivision(Division division) {
        return division;
    }

    @Override
    public void visitEqualityPostorder(Equality equality) {
        final Expression rhs = this.expressions.pop();
        final Expression lhs = this.expressions.pop();

        final Equality newEquality = new Equality(equality.getFilename(), equality.getLineNumber(), lhs, rhs);
        this.expressions.push(this.transformEquality(newEquality));
    }

    public Expression transformEquality(Equality equality) {
        return equality;
    }

    @Override
    public void visitFunctionCallInorder(FunctionCall functionCall) {
        this.argumentExpressions.push(new LinkedList<Expression>());
    }

    @Override
    public void visitFunctionCallPostorder(FunctionCall functionCall) {
        final Expression calleeExpression = this.expressions.pop();
        final List<Expression> actualsList = this.argumentExpressions.pop();
        final FunctionCall newFunctionCall = new FunctionCall(functionCall.getFilename(), functionCall.getLineNumber(),
                calleeExpression, functionCall.getFunctionIdentifier(), new ArgumentExpressions(
                        functionCall.getFilename(), functionCall.getLineNumber(), actualsList));
        this.expressions.push(this.transformFunctionCall(newFunctionCall));
    }

    public Expression transformFunctionCall(FunctionCall functionCall) {
        return functionCall;
    }

    @Override
    public void visitIfPostorder(If ifNode) {
        final Expression elseBranch = this.expressions.pop();
        final Expression thenBranch = this.expressions.pop();
        final Expression condition = this.expressions.pop();

        final If newIf = new If(ifNode.getFilename(), ifNode.getLineNumber(), condition, thenBranch, elseBranch);
        this.expressions.push(this.transformIf(newIf));
    }

    public Expression transformIf(If ifNode) {
        return ifNode;
    }

    @Override
    public void visitIsVoidPostorder(IsVoid isVoid) {
        final Expression arg = this.expressions.pop();
        final IsVoid newIsVoid = new IsVoid(isVoid.getFilename(), isVoid.getLineNumber(), arg);
        this.expressions.push(this.transformIsVoid(newIsVoid));
    }

    public Expression transformIsVoid(IsVoid isVoid) {
        return isVoid;
    }

    @Override
    public void visitLessThanPostorder(LessThan lessThan) {
        final Expression rhs = this.expressions.pop();
        final Expression lhs = this.expressions.pop();

        final LessThan newLessThan = new LessThan(lessThan.getFilename(), lessThan.getLineNumber(), lhs, rhs);
        this.expressions.push(this.transformLessThan(newLessThan));
    }

    public Expression transformLessThan(LessThan lessThan) {
        return lessThan;
    }

    @Override
    public void visitLessThanOrEqualsPostorder(LessThanOrEquals lessThanOrEquals) {
        final Expression rhs = this.expressions.pop();
        final Expression lhs = this.expressions.pop();

        final LessThanOrEquals newLessThanOrEquals = new LessThanOrEquals(lessThanOrEquals.getFilename(),
                lessThanOrEquals.getLineNumber(), lhs, rhs);
        this.expressions.push(this.transformLessThanOrEquals(newLessThanOrEquals));
    }

    public Expression transformLessThanOrEquals(LessThanOrEquals lessThanOrEquals) {
        return lessThanOrEquals;
    }

    @Override
    public void visitLetPostorder(Let let) {
        final Expression arg = this.expressions.pop();
        final Expression init = this.expressions.pop();
        final Let newLet = new Let(let.getFilename(), let.getLineNumber(), let.getVariableIdentifier(),
                let.getDeclaredType(), init, arg);
        this.expressions.push(this.transformLet(newLet));
    }

    public Expression transformLet(Let let) {
        return let;
    }

    @Override
    public void visitLoopPostorder(Loop loop) {
        final Expression body = this.expressions.pop();
        final Expression cond = this.expressions.pop();

        final Loop newLoop = new Loop(loop.getFilename(), loop.getLineNumber(), cond, body);
        this.expressions.push(this.transformLoop(newLoop));
    }

    public Expression transformLoop(Loop loop) {
        return loop;
    }

    @Override
    public void visitMultiplicationPostorder(Multiplication multiplication) {
        final Expression rhs = this.expressions.pop();
        final Expression lhs = this.expressions.pop();

        final Multiplication newMultiplication = new Multiplication(multiplication.getFilename(),
                multiplication.getLineNumber(), lhs, rhs);
        this.expressions.push(this.transformMultiplication(newMultiplication));
    }

    public Expression transformMultiplication(Multiplication multiplication) {
        return multiplication;
    }

    @Override
    public void visitNew(New newNode) {
        this.expressions.push(this.transformNew(newNode));
    }

    public Expression transformNew(New newNode) {
        return newNode;
    }

    @Override
    public void visitNoExpression(NoExpression noExpression) {
        this.expressions.push(this.transformNoExpression(noExpression));
    }

    public Expression transformNoExpression(NoExpression noExpression) {
        return noExpression;
    }

    @Override
    public void visitStaticFunctionCallInorder(StaticFunctionCall staticFunctionCall) {
        this.argumentExpressions.push(new LinkedList<Expression>());
    }

    @Override
    public void visitStaticFunctionCallPostorder(StaticFunctionCall staticFunctionCall) {
        final Expression calleeExpression = this.expressions.pop();
        final List<Expression> actualsList = this.argumentExpressions.pop();
        final StaticFunctionCall newStaticFunctionCall = new StaticFunctionCall(staticFunctionCall.getFilename(),
                staticFunctionCall.getLineNumber(), calleeExpression, staticFunctionCall.getStaticType(),
                staticFunctionCall.getFunctionIdentifier(), new ArgumentExpressions(staticFunctionCall.getFilename(),
                        staticFunctionCall.getLineNumber(), actualsList));
        this.expressions.push(this.transformStaticFunctionCall(newStaticFunctionCall));
    }

    private Expression transformStaticFunctionCall(StaticFunctionCall staticFunctionCall) {
        return staticFunctionCall;
    }

    @Override
    public void visitSubtractionPostorder(Subtraction subtraction) {
        final Expression rhs = this.expressions.pop();
        final Expression lhs = this.expressions.pop();

        final Subtraction newSubtraction = new Subtraction(subtraction.getFilename(), subtraction.getLineNumber(), lhs,
                rhs);
        this.expressions.push(this.transformSubtraction(newSubtraction));
    }

    public Expression transformSubtraction(Subtraction subtraction) {
        return subtraction;
    }

    @Override
    public void visitTypecaseInorder(Typecase typecase) {
        this.cases.push(new LinkedList<Case>());
    }

    @Override
    public void visitTypecasePostorder(Typecase typecase) {
        final Expression expr = this.expressions.pop();
        final List<Case> casesList = this.cases.pop();
        final Cases newCases = new Cases(typecase.getCases().getFilename(), typecase.getCases().getLineNumber(),
                casesList);
        final Typecase newTypecase = new Typecase(typecase.getFilename(), typecase.getLineNumber(), expr, newCases);

        this.expressions.push(this.transformTypecase(newTypecase));
    }

    public Expression transformTypecase(Typecase typecase) {
        return typecase;
    }

}
