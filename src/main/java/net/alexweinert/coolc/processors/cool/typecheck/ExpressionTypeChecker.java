package net.alexweinert.coolc.processors.cool.typecheck;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.alexweinert.coolc.representations.cool.expressions.untyped.Addition;
import net.alexweinert.coolc.representations.cool.expressions.untyped.ArgumentExpressions;
import net.alexweinert.coolc.representations.cool.expressions.untyped.ArithmeticNegation;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Assign;
import net.alexweinert.coolc.representations.cool.expressions.untyped.BlockExpressions;
import net.alexweinert.coolc.representations.cool.expressions.untyped.BoolConst;
import net.alexweinert.coolc.representations.cool.expressions.untyped.BooleanNegation;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Case;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Division;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Equality;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Expression;
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
import net.alexweinert.coolc.representations.cool.expressions.untyped.ObjectReference;
import net.alexweinert.coolc.representations.cool.expressions.untyped.StaticFunctionCall;
import net.alexweinert.coolc.representations.cool.expressions.untyped.StringConst;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Subtraction;
import net.alexweinert.coolc.representations.cool.expressions.untyped.Typecase;
import net.alexweinert.coolc.representations.cool.program.hierarchichal.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.program.hierarchichal.DefinedClassSignature;
import net.alexweinert.coolc.representations.cool.program.hierarchichal.MethodSignature;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;
import net.alexweinert.coolc.representations.cool.util.Visitor;

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
    public void visitBoolConst(BoolConst boolConst) {
        this.argumentTypes.push(ExpressionType.create(IdTable.getInstance().getBoolSymbol()));
        boolConst.setType(IdTable.getInstance().getBoolSymbol());
    }

    @Override
    public void visitIntConst(IntConst intConst) {
        this.argumentTypes.push(ExpressionType.create(IdTable.getInstance().getIntSymbol()));
        intConst.setType(IdTable.getInstance().getIntSymbol());
    }

    @Override
    public void visitStringConstant(StringConst stringConst) {
        this.argumentTypes.push(ExpressionType.create(IdTable.getInstance().getStringSymbol()));
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
            referenceType = ExpressionType.create(IdTable.getInstance().getObjectSymbol());
        }
        this.argumentTypes.push(referenceType);
        objectReference.setType(referenceType.getTypeId(this.classId));
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

        final IdSymbol argType = this.argumentTypes.pop().getTypeId(classId);
        if (!this.hierarchy.conformsTo(argType, intSymbol)) {
            err.reportTypeMismatch(arithmeticNegation, argType, intSymbol);
        }

        this.argumentTypes.push(ExpressionType.create(intSymbol));
        arithmeticNegation.setType(intSymbol);
    }

    private void visitBinaryArithmeticOperation(Expression operation) {
        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();

        final IdSymbol rhsType = this.argumentTypes.pop().getTypeId(classId);
        if (!this.hierarchy.conformsTo(rhsType, intSymbol)) {
            err.reportTypeMismatch(operation, rhsType, intSymbol);
        }

        final IdSymbol lhsType = this.argumentTypes.pop().getTypeId(classId);
        if (!this.hierarchy.conformsTo(lhsType, intSymbol)) {
            err.reportTypeMismatch(operation, lhsType, intSymbol);
        }

        this.argumentTypes.push(ExpressionType.create(intSymbol));
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

        final boolean lhsIsInt = lhsType.equals(ExpressionType.create(IdTable.getInstance().getIntSymbol()));
        final boolean lhsIsString = lhsType.equals(ExpressionType.create(IdTable.getInstance().getStringSymbol()));
        final boolean lhsIsBool = lhsType.equals(ExpressionType.create(IdTable.getInstance().getBoolSymbol()));
        final boolean lhsIsPrimitive = lhsIsInt || lhsIsString || lhsIsBool;
        final boolean rhsIsInt = rhsType.equals(ExpressionType.create(IdTable.getInstance().getIntSymbol()));
        final boolean rhsIsString = rhsType.equals(ExpressionType.create(IdTable.getInstance().getStringSymbol()));
        final boolean rhsIsBool = rhsType.equals(ExpressionType.create(IdTable.getInstance().getBoolSymbol()));
        final boolean rhsIsPrimitive = rhsIsInt || rhsIsString || rhsIsBool;
        if (lhsIsPrimitive && !lhsType.equals(rhsType)) {
            this.err.reportTypeMismatch(equality, rhsType.getTypeId(this.classId), lhsType.getTypeId(this.classId));
        } else if (rhsIsPrimitive && !lhsType.equals(rhsType)) {
            this.err.reportTypeMismatch(equality, lhsType.getTypeId(this.classId), rhsType.getTypeId(this.classId));
        }

        this.argumentTypes.push(ExpressionType.create(IdTable.getInstance().getBoolSymbol()));
        equality.setType(IdTable.getInstance().getBoolSymbol());

    }

    private void visitArithmeticComparison(Expression operation) {
        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();

        final IdSymbol rhsType = this.argumentTypes.pop().getTypeId(classId);
        if (!this.hierarchy.conformsTo(rhsType, intSymbol)) {
            err.reportTypeMismatch(operation, rhsType, intSymbol);
        }

        final IdSymbol lhsType = this.argumentTypes.pop().getTypeId(classId);
        if (!this.hierarchy.conformsTo(lhsType, intSymbol)) {
            err.reportTypeMismatch(operation, lhsType, intSymbol);
        }

        this.argumentTypes.push(ExpressionType.create(IdTable.getInstance().getBoolSymbol()));
        operation.setType(IdTable.getInstance().getBoolSymbol());
    }

    @Override
    public void visitBooleanNegationPostorder(BooleanNegation booleanNegation) {
        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();

        final IdSymbol argType = this.argumentTypes.pop().getTypeId(classId);
        if (!this.hierarchy.conformsTo(argType, boolSymbol)) {
            err.reportTypeMismatch(booleanNegation, argType, boolSymbol);
        }

        this.argumentTypes.push(ExpressionType.create(boolSymbol));
        booleanNegation.setType(boolSymbol);
    }

    @Override
    public void visitAssignPostorder(Assign assign) {
        final IdSymbol assignedVariable = assign.getVariableIdentifier();
        final ExpressionType rhsType = this.argumentTypes.pop();
        final IdSymbol rhsTypeId = rhsType.getTypeId(this.classId);
        final IdSymbol lhsTypeId = this.variablesScopes.peek().getVariableType(assignedVariable)
                .getTypeId(this.classId);

        if (this.hierarchy.conformsTo(rhsTypeId, lhsTypeId)) {
            this.argumentTypes.push(rhsType);
            assign.setType(rhsTypeId);
        } else {
            err.reportTypeMismatch(assign, rhsTypeId, lhsTypeId);
            final IdSymbol lhsDeclaredType = this.variablesScopes.peek().getVariableType(assignedVariable)
                    .getTypeId(this.classId);
            this.argumentTypes.push(ExpressionType.create(lhsDeclaredType));
            assign.setType(lhsDeclaredType);
        }
    }

    @Override
    public void visitIfPreorderOne(If ifNode) {
        final ExpressionType conditionType = this.argumentTypes.pop();
        final IdSymbol conditionTypeSymbol = conditionType.getTypeId(this.classId);
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
        ifNode.setType(leastUpperBound.getTypeId(this.classId));
    }

    @Override
    public void visitLoopInorder(Loop loop) {
        final ExpressionType conditionType = this.argumentTypes.pop();
        final IdSymbol conditionTypeSymbol = conditionType.getTypeId(this.classId);
        final IdSymbol boolTypeSymbol = IdTable.getInstance().getBoolSymbol();

        if (!conditionTypeSymbol.equals(boolTypeSymbol)) {
            this.err.reportTypeMismatch(loop.getCondition(), conditionTypeSymbol, boolTypeSymbol);
        }
    }

    @Override
    public void visitLoopPostorder(Loop loop) {
        this.argumentTypes.pop();
        this.argumentTypes.push(ExpressionType.create(IdTable.getInstance().getObjectSymbol()));
    }

    @Override
    public void visitBlockExpressionsInorder(BlockExpressions expression) {
        this.argumentTypes.pop();
    }

    @Override
    public void visitIsVoidPostorder(IsVoid isVoid) {
        this.argumentTypes.pop();
        this.argumentTypes.push(ExpressionType.create(IdTable.getInstance().getBoolSymbol()));
    }

    @Override
    public void visitLetInorder(Let let) {
        final ExpressionType initializerType = this.argumentTypes.pop();
        final IdSymbol initializerTypeSymbol = initializerType.getTypeId(this.classId);
        final IdSymbol expectedInitializerTypeSymbol = let.getDeclaredType();
        if (!this.hierarchy.conformsTo(initializerTypeSymbol, expectedInitializerTypeSymbol)) {
            this.err.reportTypeMismatch(let.getInitializer(), initializerTypeSymbol, expectedInitializerTypeSymbol);
        }
        final VariablesScope currentScope = this.variablesScopes.peek();
        this.variablesScopes.push(currentScope.addVariable(let.getVariableIdentifier(), let.getDeclaredType()));
    }

    @Override
    public void visitLetPostorder(Let let) {
        this.variablesScopes.pop();
        let.setType(this.argumentTypes.peek().getTypeId(this.classId));
    }

    @Override
    public void visitFunctionCallInorder(FunctionCall call) {
        final IdSymbol calleeType = this.argumentTypes.pop().getTypeId(classId);
        this.pushArgumentTypes(call, calleeType);
    }

    @Override
    public void visitFunctionCallPostorder(FunctionCall call) {
        this.methodSignatures.pop();
        this.methodDefiningClasses.pop();
        this.argumentTypes.push(this.methodReturnTypes.pop());
        call.setType(this.argumentTypes.peek().getTypeId(this.classId));
    }

    @Override
    public void visitStaticFunctionCallInorder(StaticFunctionCall call) {
        final IdSymbol calleeType = this.argumentTypes.pop().getTypeId(classId);
        final IdSymbol declaredCalleeType = call.getStaticType();
        if (!this.hierarchy.conformsTo(calleeType, declaredCalleeType)) {
            this.err.reportTypeMismatch(call.getCallee(), calleeType, declaredCalleeType);
        }

        this.pushArgumentTypes(call, declaredCalleeType);
    }

    @Override
    public void visitStaticFunctionCallPostorder(StaticFunctionCall call) {
        this.methodSignatures.pop();
        this.methodDefiningClasses.pop();
        this.argumentTypes.push(this.methodReturnTypes.pop());
        call.setType(this.argumentTypes.peek().getTypeId(this.classId));
    }

    @Override
    public void visitNew(New newNode) {
        this.argumentTypes.push(ExpressionType.create(newNode.getTypeIdentifier()));
        newNode.setType(newNode.getTypeIdentifier());
    }

    /**
     * Updates this.methodSignatures to contain the declared argument types for the given call, where the callee
     * expression is of the given type. Reports errors if the number of given arguments does not conform to the number
     * of declared arguments.
     */
    private void pushArgumentTypes(FunctionCall call, final IdSymbol calleeType) {
        this.methodDefiningClasses.push(calleeType);

        final DefinedClassSignature calleeSignature = this.definedSignatures.get(calleeType);
        final MethodSignature methodSignature = calleeSignature.getMethodSignature(call.getFunctionIdentifier());

        final List<ExpressionType> argumentTypes = new LinkedList<>();
        final boolean methodDefined = methodSignature != null;
        if (methodDefined) {
            this.methodReturnTypes.push(ExpressionType.create(methodSignature.getReturnType()));

            final int declaredNumberOfArgs = methodSignature.getArgumentTypes().size();
            final int givenNumberOfArgs = call.getArguments().size();
            final boolean correctNumberOfArguments = declaredNumberOfArgs == givenNumberOfArgs;

            if (correctNumberOfArguments) {
                for (IdSymbol argType : methodSignature.getArgumentTypes()) {
                    argumentTypes.add(ExpressionType.create(argType));
                }
            } else {
                this.err.reportWrongNumberOfFunctionArguments(call, methodSignature.getArgumentTypes().size());
                for (int i = 0; i < givenNumberOfArgs; ++i) {
                    argumentTypes.add(ExpressionType.create(IdTable.getInstance().getObjectSymbol()));
                }
            }
        } else {
            this.err.reportUndefinedMethod(call, calleeType);
            for (int i = 0; i < call.getArguments().size(); ++i) {
                argumentTypes.add(ExpressionType.create(IdTable.getInstance().getObjectSymbol()));
            }
        }
        this.methodSignatures.push(argumentTypes);
    }

    @Override
    public void visitArgumentExpressionsInorder(ArgumentExpressions expressions) {
        final IdSymbol givenArgumentType = this.argumentTypes.pop().getTypeId(this.classId);
        final IdSymbol expectedArgumentType = this.methodSignatures.peek().get(0)
                .getTypeId(this.methodDefiningClasses.peek());
        this.methodSignatures.peek().remove(0);
        if (!this.hierarchy.conformsTo(givenArgumentType, expectedArgumentType)) {
            this.err.reportTypeMismatch(expressions, givenArgumentType, expectedArgumentType);
        }
    }

    @Override
    public void visitArgumentExpressionsPostorder(ArgumentExpressions expressions) {
        if (expressions.size() == 0) {
            return;
        }
        final IdSymbol givenArgumentType = this.argumentTypes.pop().getTypeId(this.classId);
        final IdSymbol expectedArgumentType = this.methodSignatures.peek().get(0)
                .getTypeId(this.methodDefiningClasses.peek());
        this.methodSignatures.peek().remove(0);
        if (!this.hierarchy.conformsTo(givenArgumentType, expectedArgumentType)) {
            this.err.reportTypeMismatch(expressions, givenArgumentType, expectedArgumentType);
        }
    }

    @Override
    public void visitTypecasePostorder(Typecase typecase) {
        final List<ExpressionType> branchTypes = new LinkedList<>();
        for (int i = 0; i < typecase.getCases().size(); ++i) {
            branchTypes.add(this.argumentTypes.pop());
        }

        final Iterator<ExpressionType> typeIterator = branchTypes.iterator();
        ExpressionType leastUpperBound = typeIterator.next();
        while (typeIterator.hasNext()) {
            leastUpperBound = leastUpperBound.computeLeastUpperBound(typeIterator.next(), this.classId, this.hierarchy);
        }
        this.argumentTypes.push(leastUpperBound);
        typecase.setType(leastUpperBound.getTypeId(this.classId));
    }

    @Override
    public void visitCasePreorder(Case caseNode) {
        this.variablesScopes.push(this.variablesScopes.peek().addVariable(caseNode.getVariableIdentifier(),
                caseNode.getDeclaredType()));
    }

    @Override
    public void visitCasePostorder(Case caseNode) {
        this.variablesScopes.pop();
    }
}
