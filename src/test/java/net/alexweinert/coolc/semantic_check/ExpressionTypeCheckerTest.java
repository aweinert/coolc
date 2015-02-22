package net.alexweinert.coolc.semantic_check;

import java.util.HashMap;
import java.util.Map;

import net.alexweinert.coolc.program.ast.ASTFactory;
import net.alexweinert.coolc.program.ast.Expression;
import net.alexweinert.coolc.program.ast.ObjectReference;
import net.alexweinert.coolc.program.information.ClassHierarchy;
import net.alexweinert.coolc.program.information.DefinedClassSignature;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ExpressionTypeCheckerTest {

    ClassHierarchy hierarchy;

    @Before
    public void defineStandardHierarchy() {
        this.hierarchy = Mockito.mock(ClassHierarchy.class);
        final IdSymbol objectSymbol = IdTable.getInstance().getObjectSymbol();
        Mockito.when(hierarchy.conformsTo(objectSymbol, objectSymbol)).thenReturn(true);

        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();
        Mockito.when(hierarchy.conformsTo(intSymbol, intSymbol)).thenReturn(true);

        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();
        Mockito.when(hierarchy.conformsTo(boolSymbol, boolSymbol)).thenReturn(true);

        final IdSymbol stringSymbol = IdTable.getInstance().getStringSymbol();
        Mockito.when(hierarchy.conformsTo(stringSymbol, stringSymbol)).thenReturn(true);
    }

    @Test
    public void testIntConstant() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.intConst(3);
        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();

        this.testWelltypedVariableFreeExpression(intSymbol, testExpression);
    }

    @Test
    public void testBoolConstant() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.boolConst(true);
        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();

        this.testWelltypedVariableFreeExpression(boolSymbol, testExpression);
    }

    @Test
    public void testStringConstant() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.stringConst("Hello World");
        final IdSymbol stringSymbol = IdTable.getInstance().getStringSymbol();

        this.testWelltypedVariableFreeExpression(stringSymbol, testExpression);
    }

    @Test
    public void testWelltypedAddition() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.add(factory.intConst(3), factory.intConst(5));
        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();

        this.testWelltypedVariableFreeExpression(intSymbol, testExpression);
    }

    @Test
    public void testWelltypedSubtraction() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.sub(factory.intConst(3), factory.intConst(5));
        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();

        this.testWelltypedVariableFreeExpression(intSymbol, testExpression);
    }

    @Test
    public void testWelltypedMultiplication() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.mult(factory.intConst(3), factory.intConst(5));
        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();

        this.testWelltypedVariableFreeExpression(intSymbol, testExpression);
    }

    @Test
    public void testWelltypedDivision() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.div(factory.intConst(3), factory.intConst(5));
        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();

        this.testWelltypedVariableFreeExpression(intSymbol, testExpression);
    }

    @Test
    public void testWelltypedLessThan() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.lt(factory.intConst(3), factory.intConst(5));
        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();

        this.testWelltypedVariableFreeExpression(boolSymbol, testExpression);
    }

    @Test
    public void testWelltypedLessThanEquals() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.le(factory.intConst(3), factory.intConst(5));
        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();

        this.testWelltypedVariableFreeExpression(boolSymbol, testExpression);
    }

    @Test
    public void testWelltypedObjectReference() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.varRef("foo");
        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();

        final IdSymbol variableId = IdTable.getInstance().addString("foo");
        final VariablesScope scope = Mockito.mock(VariablesScope.class);
        Mockito.when(scope.getVariableType(variableId)).thenReturn(ExpressionType.create(boolSymbol));
        Mockito.when(scope.containsVariable(variableId)).thenReturn(true);

        this.testWelltypedExpression(boolSymbol, testExpression, scope);
    }

    @Test
    public void testObjectReferenceOutOfScope() {
        final ASTFactory factory = new ASTFactory();
        final ObjectReference testExpression = factory.varRef("foo");

        final VariablesScope scope = Mockito.mock(VariablesScope.class);

        final IdSymbol classId = IdTable.getInstance().addString("TestClass");

        final Map<IdSymbol, DefinedClassSignature> definedSignatures = new HashMap<>();

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);

        final ExpressionTypeChecker checker = new ExpressionTypeChecker(classId, scope, hierarchy, definedSignatures,
                err);
        testExpression.acceptVisitor(checker);

        Mockito.verify(err).reportVariableOutOfScope(testExpression);
        Mockito.verifyNoMoreInteractions(err);
    }

    @Test
    public void testWelltypedAssignment() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.assignment("foo", factory.intConst(3));
        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();

        final VariablesScope scope = Mockito.mock(VariablesScope.class);
        Mockito.when(scope.getVariableType(IdTable.getInstance().addString("foo"))).thenReturn(
                ExpressionType.create(intSymbol));

        this.testWelltypedExpression(intSymbol, testExpression, scope);
    }

    private void testWelltypedVariableFreeExpression(IdSymbol expectedType, Expression testExpression) {
        final VariablesScope initialScope = Mockito.mock(VariablesScope.class);

        this.testWelltypedExpression(expectedType, testExpression, initialScope);
    }

    private void testWelltypedExpression(IdSymbol expectedType, Expression testExpression, VariablesScope initialScope) {
        final IdSymbol classId = IdTable.getInstance().addString("TestClass");

        final Map<IdSymbol, DefinedClassSignature> definedSignatures = new HashMap<>();

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);

        final ExpressionTypeChecker checker = new ExpressionTypeChecker(classId, initialScope, hierarchy,
                definedSignatures, err);
        testExpression.acceptVisitor(checker);

        Assert.assertEquals(expectedType, checker.getResultType().getTypeId(classId));

        Mockito.verifyZeroInteractions(err);
    }
}
