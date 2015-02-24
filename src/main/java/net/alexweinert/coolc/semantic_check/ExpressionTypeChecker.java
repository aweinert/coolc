package net.alexweinert.coolc.semantic_check;

import java.util.Map;
import java.util.Stack;

import net.alexweinert.coolc.program.ast.Addition;
import net.alexweinert.coolc.program.ast.ArithmeticNegation;
import net.alexweinert.coolc.program.ast.Assign;
import net.alexweinert.coolc.program.ast.BlockExpressions;
import net.alexweinert.coolc.program.ast.BoolConst;
import net.alexweinert.coolc.program.ast.BooleanNegation;
import net.alexweinert.coolc.program.ast.Division;
import net.alexweinert.coolc.program.ast.Expression;
import net.alexweinert.coolc.program.ast.If;
import net.alexweinert.coolc.program.ast.IntConst;
import net.alexweinert.coolc.program.ast.IsVoid;
import net.alexweinert.coolc.program.ast.LessThan;
import net.alexweinert.coolc.program.ast.LessThanOrEquals;
import net.alexweinert.coolc.program.ast.Loop;
import net.alexweinert.coolc.program.ast.Multiplication;
import net.alexweinert.coolc.program.ast.ObjectReference;
import net.alexweinert.coolc.program.ast.StringConst;
import net.alexweinert.coolc.program.ast.Subtraction;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.information.ClassHierarchy;
import net.alexweinert.coolc.program.information.DefinedClassSignature;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

class ExpressionTypeChecker extends ASTVisitor {

    final private Stack<ExpressionType> argumentTypes = new Stack<>();

    final private IdSymbol classId;
    final private Stack<VariablesScope> variablesScopes;
    final private ClassHierarchy hierarchy;
    final private Map<IdSymbol, DefinedClassSignature> definedSignatures;
    final private SemanticErrorReporter err;

    public ExpressionTypeChecker(IdSymbol classId, VariablesScope initialScope, ClassHierarchy hierarchy,
            Map<IdSymbol, DefinedClassSignature> definedSignatures, SemanticErrorReporter err) {
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
    }

    @Override
    public void visitIntConst(IntConst intConst) {
        this.argumentTypes.push(ExpressionType.create(IdTable.getInstance().getIntSymbol()));
    }

    @Override
    public void visitStringConstant(StringConst stringConst) {
        this.argumentTypes.push(ExpressionType.create(IdTable.getInstance().getStringSymbol()));
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
    }

    @Override
    public void visitLessThanPostorder(LessThan lessThan) {
        this.visitArithmeticComparison(lessThan);
    }

    @Override
    public void visitLessThanOrEqualsPostorder(LessThanOrEquals lessThanOrEquals) {
        this.visitArithmeticComparison(lessThanOrEquals);
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
    }

    @Override
    public void visitBooleanNegationPostorder(BooleanNegation booleanNegation) {
        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();

        final IdSymbol argType = this.argumentTypes.pop().getTypeId(classId);
        if (!this.hierarchy.conformsTo(argType, boolSymbol)) {
            err.reportTypeMismatch(booleanNegation, argType, boolSymbol);
        }

        this.argumentTypes.push(ExpressionType.create(boolSymbol));
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
        } else {
            err.reportTypeMismatch(assign, rhsTypeId, lhsTypeId);
            final IdSymbol lhsDeclaredType = this.variablesScopes.peek().getVariableType(assignedVariable)
                    .getTypeId(this.classId);
            this.argumentTypes.push(ExpressionType.create(lhsDeclaredType));
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
}
