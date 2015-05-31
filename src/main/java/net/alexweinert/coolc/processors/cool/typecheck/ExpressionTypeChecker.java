package net.alexweinert.coolc.processors.cool.typecheck;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.alexweinert.coolc.representations.cool.ast.Addition;
import net.alexweinert.coolc.representations.cool.ast.ArgumentExpressions;
import net.alexweinert.coolc.representations.cool.ast.ArithmeticNegation;
import net.alexweinert.coolc.representations.cool.ast.Assign;
import net.alexweinert.coolc.representations.cool.ast.BlockExpressions;
import net.alexweinert.coolc.representations.cool.ast.BoolConst;
import net.alexweinert.coolc.representations.cool.ast.BooleanNegation;
import net.alexweinert.coolc.representations.cool.ast.Case;
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
import net.alexweinert.coolc.representations.cool.information.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.information.DefinedClassSignature;
import net.alexweinert.coolc.representations.cool.information.MethodSignature;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

class ExpressionTypeChecker extends Visitor {

    final private Stack<ExpressionType> argumentTypes = new Stack<>();
    final private Stack<List<ExpressionType>> methodSignatures = new Stack<>();
    final private Stack<IdSymbol> methodDefiningClasses = new Stack<>();
    final private Stack<ExpressionType> methodReturnTypes = new Stack<>();

    final private IdSymbol classId;
    final private Stack<VariablesScope> variablesScopes;
    final private ClassHierarchy hierarchy;
    final private Map<IdSymbol, DefinedClassSignature> definedSignatures;
    final private TypeErrorReporter err;

    public ExpressionTypeChecker(IdSymbol classId, VariablesScope initialScope, ClassHierarchy hierarchy,
            Map<IdSymbol, DefinedClassSignature> definedSignatures, TypeErrorReporter err) {
        this.classId = classId;
        this.variablesScopes = new Stack<>();
        this.variablesScopes.add(initialScope);
        this.hierarchy = hierarchy;
        this.definedSignatures = definedSignatures;
        this.err = err;
    }

    public ExpressionType getResultType() {
        assert this.argumentTypes.size() == 1;
        return this.argumentTypes.peek();
    }

    @Override
    public void visitNoExpression(NoExpression noExpression) {
        this.argumentTypes.push(ExpressionType.createNoExpressionType());
    }

    @Override
    public void visitBoolConst(BoolConst boolConst) {
        this.argumentTypes.push(ExpressionType.createBoolType());
        boolConst.setType(IdTable.getInstance().getBoolSymbol());
    }

    @Override
    public void visitIntConst(IntConst intConst) {
        this.argumentTypes.push(ExpressionType.createIntType());
        intConst.setType(IdTable.getInstance().getIntSymbol());
    }

    @Override
    public void visitStringConstant(StringConst stringConst) {
        this.argumentTypes.push(ExpressionType.createStringType());
        stringConst.setType(IdTable.getInstance().getStringSymbol());
    }

    @Override
    public void visitObjectReference(ObjectReference objectReference) {
        final VariablesScope scope = this.variablesScopes.peek();
        final ExpressionType referenceType;
        if (scope.containsVariable(objectReference.getVariableIdentifier())) {
            referenceType = scope.getVariableType(objectReference.getVariableIdentifier());
        } else {
            err.reportVariableOutOfScope(objectReference);
            referenceType = ExpressionType.createObjectType();
        }
        this.argumentTypes.push(referenceType);
        objectReference.setType(referenceType.getTypeId());
    }

    @Override
    public void visitAdditionPostorder(Addition addition) {
        this.visitBinaryArithmeticOperation(addition);
    }

    @Override
    public void visitDivisionPostorder(Division division) {
        this.visitBinaryArithmeticOperation(division);
    }

    @Override
    public void visitMultiplicationPostorder(Multiplication multiplication) {
        this.visitBinaryArithmeticOperation(multiplication);
    }

    @Override
    public void visitSubtractionPostorder(Subtraction subtraction) {
        this.visitBinaryArithmeticOperation(subtraction);
    }

    @Override
    public void visitArithmeticNegationPostOrder(ArithmeticNegation arithmeticNegation) {
        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();

        final ExpressionType argType = this.argumentTypes.pop();
        if (!argType.conformsTo(ExpressionType.createIntType(), hierarchy)) {
            err.reportTypeMismatch(arithmeticNegation, argType.getTypeId(), intSymbol);
        }

        this.argumentTypes.push(ExpressionType.createIntType());
        arithmeticNegation.setType(intSymbol);
    }

    private void visitBinaryArithmeticOperation(Expression operation) {
        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();

        final ExpressionType rhsType = this.argumentTypes.pop();
        if (!rhsType.conformsTo(ExpressionType.createIntType(), this.hierarchy)) {
            err.reportTypeMismatch(operation, rhsType.getTypeId(), intSymbol);
        }

        final ExpressionType lhsType = this.argumentTypes.pop();
        if (!lhsType.conformsTo(ExpressionType.createIntType(), this.hierarchy)) {
            err.reportTypeMismatch(operation, lhsType.getTypeId(), intSymbol);
        }

        this.argumentTypes.push(ExpressionType.createIntType());
        operation.setType(intSymbol);
    }

    @Override
    public void visitLessThanPostorder(LessThan lessThan) {
        this.visitArithmeticComparison(lessThan);
    }

    @Override
    public void visitLessThanOrEqualsPostorder(LessThanOrEquals lessThanOrEquals) {
        this.visitArithmeticComparison(lessThanOrEquals);
    }

    @Override
    public void visitEqualityPostorder(Equality equality) {
        final ExpressionType rhsType = this.argumentTypes.pop();
        final ExpressionType lhsType = this.argumentTypes.pop();

        final boolean lhsIsBool = lhsType.equals(ExpressionType.createBoolType());
        final boolean lhsIsInt = lhsType.equals(ExpressionType.createIntType());
        final boolean lhsIsString = lhsType.equals(ExpressionType.createStringType());
        final boolean lhsIsPrimitive = lhsIsInt || lhsIsString || lhsIsBool;

        final boolean rhsIsBool = rhsType.equals(ExpressionType.createBoolType());
        final boolean rhsIsInt = rhsType.equals(ExpressionType.createIntType());
        final boolean rhsIsString = rhsType.equals(ExpressionType.createStringType());
        final boolean rhsIsPrimitive = rhsIsInt || rhsIsString || rhsIsBool;

        if (lhsIsPrimitive && !lhsType.equals(rhsType)) {
            this.err.reportTypeMismatch(equality, rhsType.getTypeId(), lhsType.getTypeId());
        } else if (rhsIsPrimitive && !lhsType.equals(rhsType)) {
            this.err.reportTypeMismatch(equality, lhsType.getTypeId(), rhsType.getTypeId());
        }

        this.argumentTypes.push(ExpressionType.createBoolType());
        equality.setType(IdTable.getInstance().getBoolSymbol());

    }

    private void visitArithmeticComparison(Expression operation) {
        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();

        final ExpressionType rhsType = this.argumentTypes.pop();
        if (!rhsType.conformsTo(ExpressionType.createIntType(), this.hierarchy)) {
            err.reportTypeMismatch(operation, rhsType.getTypeId(), intSymbol);
        }

        final ExpressionType lhsType = this.argumentTypes.pop();
        if (!lhsType.conformsTo(ExpressionType.createIntType(), this.hierarchy)) {
            err.reportTypeMismatch(operation, lhsType.getTypeId(), intSymbol);
        }

        this.argumentTypes.push(ExpressionType.createBoolType());
        operation.setType(IdTable.getInstance().getBoolSymbol());
    }

    @Override
    public void visitBooleanNegationPostorder(BooleanNegation booleanNegation) {
        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();

        final ExpressionType argType = this.argumentTypes.pop();
        if (!argType.conformsTo(ExpressionType.createBoolType(), this.hierarchy)) {
            err.reportTypeMismatch(booleanNegation, argType.getTypeId(), boolSymbol);
        }

        this.argumentTypes.push(ExpressionType.createBoolType());
        booleanNegation.setType(boolSymbol);
    }

    @Override
    public void visitAssignPostorder(Assign assign) {
        final IdSymbol assignedVariable = assign.getVariableIdentifier();
        final ExpressionType rhsType = this.argumentTypes.pop();
        final ExpressionType lhsType = this.variablesScopes.peek().getVariableType(assignedVariable);

        if (rhsType.conformsTo(lhsType, this.hierarchy)) {
            this.argumentTypes.push(rhsType);
            assign.setType(rhsType.getTypeId());
        } else {
            err.reportTypeMismatch(assign, rhsType.getTypeId(), lhsType.getTypeId());
            final ExpressionType lhsDeclaredType = this.variablesScopes.peek().getVariableType(assignedVariable);
            this.argumentTypes.push(lhsDeclaredType);
            assign.setType(lhsDeclaredType.getTypeId());
        }
    }

    @Override
    public void visitIfPreorderOne(If ifNode) {
        final ExpressionType conditionType = this.argumentTypes.pop();
        final IdSymbol conditionTypeSymbol = conditionType.getTypeId();
        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();
        if (!conditionTypeSymbol.equals(boolSymbol)) {
            err.reportTypeMismatch(ifNode.getCondition(), conditionTypeSymbol, boolSymbol);
        }
    };

    @Override
    public void visitIfPostorder(If ifNode) {
        final ExpressionType elseBranchType = this.argumentTypes.pop();
        final ExpressionType thenBranchType = this.argumentTypes.pop();
        final ExpressionType leastUpperBound = thenBranchType.computeLeastUpperBound(elseBranchType, this.classId,
                this.hierarchy);

        this.argumentTypes.push(leastUpperBound);
        ifNode.setType(leastUpperBound.getTypeId());
    }

    @Override
    public void visitLoopInorder(Loop loop) {
        final ExpressionType conditionType = this.argumentTypes.pop();
        final IdSymbol conditionTypeSymbol = conditionType.getTypeId();
        final IdSymbol boolTypeSymbol = IdTable.getInstance().getBoolSymbol();

        if (!conditionTypeSymbol.equals(boolTypeSymbol)) {
            this.err.reportTypeMismatch(loop.getCondition(), conditionTypeSymbol, boolTypeSymbol);
        }
    }

    @Override
    public void visitLoopPostorder(Loop loop) {
        this.argumentTypes.pop();
        this.argumentTypes.push(ExpressionType.createObjectType());
    }

    @Override
    public void visitBlockExpressionsInorder(BlockExpressions expression) {
        this.argumentTypes.pop();
    }

    @Override
    public void visitIsVoidPostorder(IsVoid isVoid) {
        this.argumentTypes.pop();
        this.argumentTypes.push(ExpressionType.createBoolType());
    }

    @Override
    public void visitLetInorder(Let let) {
        final ExpressionType initializerType = this.argumentTypes.pop();
        final IdSymbol expectedInitializerTypeSymbol = let.getDeclaredType();
        final ExpressionType expectedInitializerType = ExpressionType.create(expectedInitializerTypeSymbol,
                this.classId);
        if (!initializerType.conformsTo(expectedInitializerType, this.hierarchy)) {
            this.err.reportTypeMismatch(let.getInitializer(), initializerType.getTypeId(),
                    expectedInitializerTypeSymbol);
        }
        final VariablesScope currentScope = this.variablesScopes.peek();
        this.variablesScopes.push(currentScope.addVariable(this.classId, let.getVariableIdentifier(),
                let.getDeclaredType()));
    }

    @Override
    public void visitLetPostorder(Let let) {
        this.variablesScopes.pop();
        let.setType(this.argumentTypes.peek().getTypeId());
    }

    @Override
    public void visitFunctionCallInorder(FunctionCall call) {
        final ExpressionType calleeType = this.argumentTypes.pop();
        this.pushArgumentTypes(call, calleeType);
    }

    @Override
    public void visitFunctionCallPostorder(FunctionCall call) {
        this.methodSignatures.pop();
        this.methodDefiningClasses.pop();
        this.argumentTypes.push(this.methodReturnTypes.pop());
        call.setType(this.argumentTypes.peek().getTypeId());
    }

    @Override
    public void visitStaticFunctionCallInorder(StaticFunctionCall call) {
        final ExpressionType calleeType = this.argumentTypes.pop();
        final ExpressionType declaredCalleeType = ExpressionType.create(call.getStaticType(), this.classId);
        if (!calleeType.conformsTo(declaredCalleeType, this.hierarchy)) {
            this.err.reportTypeMismatch(call.getCallee(), calleeType.getTypeId(), declaredCalleeType.getTypeId());
        }

        this.pushArgumentTypes(call, declaredCalleeType);
    }

    @Override
    public void visitStaticFunctionCallPostorder(StaticFunctionCall call) {
        this.methodSignatures.pop();
        this.methodDefiningClasses.pop();
        this.argumentTypes.push(this.methodReturnTypes.pop());
        call.setType(this.argumentTypes.peek().getTypeId());
    }

    @Override
    public void visitNew(New newNode) {
        this.argumentTypes.push(ExpressionType.create(newNode.getTypeIdentifier(), this.classId));
        newNode.setType(newNode.getTypeIdentifier());
    }

    /**
     * Updates this.methodSignatures to contain the declared argument types for the given call, where the callee
     * expression is of the given type. Reports errors if the number of given arguments does not conform to the number
     * of declared arguments.
     */
    private void pushArgumentTypes(FunctionCall call, final ExpressionType calleeType) {
        this.methodDefiningClasses.push(calleeType.getTypeId());

        final DefinedClassSignature calleeSignature = this.definedSignatures.get(calleeType.getTypeId());
        final MethodSignature methodSignature = calleeSignature.getMethodSignature(call.getFunctionIdentifier());

        final List<ExpressionType> argumentTypes = new LinkedList<>();
        final boolean methodDefined = methodSignature != null;
        if (methodDefined) {
            this.methodReturnTypes.push(ExpressionType.create(methodSignature.getReturnType(), calleeType.getTypeId()));

            final int declaredNumberOfArgs = methodSignature.getArgumentTypes().size();
            final int givenNumberOfArgs = call.getArguments().size();
            final boolean correctNumberOfArguments = declaredNumberOfArgs == givenNumberOfArgs;

            if (correctNumberOfArguments) {
                for (IdSymbol argType : methodSignature.getArgumentTypes()) {
                    argumentTypes.add(ExpressionType.create(argType, calleeType.getTypeId()));
                }
            } else {
                this.err.reportWrongNumberOfFunctionArguments(call, methodSignature.getArgumentTypes().size());
                for (int i = 0; i < givenNumberOfArgs; ++i) {
                    argumentTypes.add(ExpressionType.create(IdTable.getInstance().getObjectSymbol(),
                            calleeType.getTypeId()));
                }
            }
        } else {
            this.err.reportUndefinedMethod(call, calleeType.getTypeId());
            for (int i = 0; i < call.getArguments().size(); ++i) {
                argumentTypes
                        .add(ExpressionType.create(IdTable.getInstance().getObjectSymbol(), calleeType.getTypeId()));
            }
        }
        this.methodSignatures.push(argumentTypes);
    }

    @Override
    public void visitArgumentExpressionsInorder(ArgumentExpressions expressions) {
        final ExpressionType givenArgumentType = this.argumentTypes.pop();
        final ExpressionType expectedArgumentType = this.methodSignatures.peek().get(0);
        this.methodSignatures.peek().remove(0);
        if (!givenArgumentType.conformsTo(expectedArgumentType, this.hierarchy)) {
            this.err.reportTypeMismatch(expressions, givenArgumentType.getTypeId(), expectedArgumentType.getTypeId());
        }
    }

    @Override
    public void visitArgumentExpressionsPostorder(ArgumentExpressions expressions) {
        if (expressions.size() == 0) {
            return;
        }
        final ExpressionType givenArgumentType = this.argumentTypes.pop();
        final ExpressionType expectedArgumentType = this.methodSignatures.peek().get(0);
        this.methodSignatures.peek().remove(0);
        if (!givenArgumentType.conformsTo(expectedArgumentType, this.hierarchy)) {
            this.err.reportTypeMismatch(expressions, givenArgumentType.getTypeId(), expectedArgumentType.getTypeId());
        }
    }

    @Override
    public void visitTypecasePostorder(Typecase typecase) {
        final List<ExpressionType> branchTypes = new LinkedList<>();
        for (int i = 0; i < typecase.getCases().size(); ++i) {
            branchTypes.add(this.argumentTypes.pop());
        }

        this.argumentTypes.pop();

        final Iterator<ExpressionType> typeIterator = branchTypes.iterator();
        ExpressionType leastUpperBound = typeIterator.next();
        while (typeIterator.hasNext()) {
            leastUpperBound = leastUpperBound.computeLeastUpperBound(typeIterator.next(), this.classId, this.hierarchy);
        }
        this.argumentTypes.push(leastUpperBound);
        typecase.setType(leastUpperBound.getTypeId());
    }

    @Override
    public void visitCasePreorder(Case caseNode) {
        this.variablesScopes.push(this.variablesScopes.peek().addVariable(this.classId,
                caseNode.getVariableIdentifier(), caseNode.getDeclaredType()));
    }

    @Override
    public void visitCasePostorder(Case caseNode) {
        this.variablesScopes.pop();
    }
}
