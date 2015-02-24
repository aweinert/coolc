package net.alexweinert.coolc.semantic_check;

import java.util.HashMap;
import java.util.Map;

import net.alexweinert.coolc.program.ast.ASTFactory;
import net.alexweinert.coolc.program.ast.Expression;
import net.alexweinert.coolc.program.ast.If;
import net.alexweinert.coolc.program.ast.Loop;
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
        Mockito.when(hierarchy.getLeastUpperBound(objectSymbol, objectSymbol)).thenReturn(objectSymbol);

        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();
        Mockito.when(hierarchy.conformsTo(intSymbol, intSymbol)).thenReturn(true);
        Mockito.when(hierarchy.getLeastUpperBound(intSymbol, intSymbol)).thenReturn(intSymbol);

        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();
        Mockito.when(hierarchy.conformsTo(boolSymbol, boolSymbol)).thenReturn(true);
        Mockito.when(hierarchy.getLeastUpperBound(boolSymbol, boolSymbol)).thenReturn(boolSymbol);

        final IdSymbol stringSymbol = IdTable.getInstance().getStringSymbol();
        Mockito.when(hierarchy.conformsTo(stringSymbol, stringSymbol)).thenReturn(true);
        Mockito.when(hierarchy.getLeastUpperBound(stringSymbol, stringSymbol)).thenReturn(stringSymbol);
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

        final IdSymbol objectSymbol = IdTable.getInstance().getObjectSymbol();

        final SemanticErrorReporter err = this.testIlltypedExpression(objectSymbol, testExpression, scope);

        Mockito.verify(err).reportVariableOutOfScope(testExpression);
        Mockito.verifyNoMoreInteractions(err);
    }

    @Test
    /** Tests that an assignment of the type x : A <- y : A has type A */
    public void testSameTypeAssignment() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.assignment("foo", factory.intConst(3));
        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();

        final VariablesScope scope = Mockito.mock(VariablesScope.class);
        Mockito.when(scope.getVariableType(IdTable.getInstance().addString("foo"))).thenReturn(
                ExpressionType.create(intSymbol));

        this.testWelltypedExpression(intSymbol, testExpression, scope);
    }

    @Test
    /** Tests that an assignment of the type x : A <- y : B has type B, where A < B */
    public void testSubtypeAssignment() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.assignment("x", factory.varRef("y"));

        final IdSymbol classASymbol = IdTable.getInstance().addString("ClassA");
        final IdSymbol classBSymbol = IdTable.getInstance().addString("ClassB");

        final VariablesScope scope = Mockito.mock(VariablesScope.class);

        final IdSymbol xSymbol = IdTable.getInstance().addString("x");
        Mockito.when(scope.containsVariable(xSymbol)).thenReturn(true);
        Mockito.when(scope.getVariableType(xSymbol)).thenReturn(ExpressionType.create(classASymbol));

        final IdSymbol ySymbol = IdTable.getInstance().addString("y");
        Mockito.when(scope.containsVariable(ySymbol)).thenReturn(true);
        Mockito.when(scope.getVariableType(ySymbol)).thenReturn(ExpressionType.create(classBSymbol));

        Mockito.when(this.hierarchy.conformsTo(classBSymbol, classASymbol)).thenReturn(true);

        this.testWelltypedExpression(classBSymbol, testExpression, scope);
    }

    @Test
    public void testIlltypedAssignment() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.assignment("x", factory.varRef("y"));

        final IdSymbol classASymbol = IdTable.getInstance().addString("ClassA");
        final IdSymbol classBSymbol = IdTable.getInstance().addString("ClassB");

        final VariablesScope scope = Mockito.mock(VariablesScope.class);

        final IdSymbol xSymbol = IdTable.getInstance().addString("x");
        Mockito.when(scope.containsVariable(xSymbol)).thenReturn(true);
        Mockito.when(scope.getVariableType(xSymbol)).thenReturn(ExpressionType.create(classASymbol));

        final IdSymbol ySymbol = IdTable.getInstance().addString("y");
        Mockito.when(scope.containsVariable(ySymbol)).thenReturn(true);
        Mockito.when(scope.getVariableType(ySymbol)).thenReturn(ExpressionType.create(classBSymbol));

        Mockito.when(this.hierarchy.conformsTo(classBSymbol, classASymbol)).thenReturn(false);

        SemanticErrorReporter err = this.testIlltypedExpression(classASymbol, testExpression, scope);

        Mockito.verify(err).reportTypeMismatch(testExpression, classBSymbol, classASymbol);
        Mockito.verifyNoMoreInteractions(err);
    }

    @Test
    public void testWelltypedIfStatementSameType() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.ifStatement(factory.boolConst(true), factory.intConst(2),
                factory.intConst(5));

        final IdSymbol intTypeSymbol = IdTable.getInstance().getIntSymbol();

        this.testWelltypedVariableFreeExpression(intTypeSymbol, testExpression);
    }

    @Test
    public void testWelltypedIfStatementSubtype() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.ifStatement(factory.boolConst(true), factory.varRef("x"),
                factory.varRef("y"));

        final VariablesScope initialScope = Mockito.mock(VariablesScope.class);
        final IdSymbol xSymbol = IdTable.getInstance().addString("x");
        final IdSymbol classASymbol = IdTable.getInstance().addString("ClassA");
        Mockito.when(initialScope.getVariableType(xSymbol)).thenReturn(ExpressionType.create(classASymbol));
        Mockito.when(initialScope.containsVariable(xSymbol)).thenReturn(true);

        final IdSymbol ySymbol = IdTable.getInstance().addString("y");
        final IdSymbol classBSymbol = IdTable.getInstance().addString("ClassB");
        Mockito.when(initialScope.getVariableType(ySymbol)).thenReturn(ExpressionType.create(classBSymbol));
        Mockito.when(initialScope.containsVariable(ySymbol)).thenReturn(true);

        Mockito.when(this.hierarchy.getLeastUpperBound(classASymbol, classBSymbol)).thenReturn(classASymbol);
        Mockito.when(this.hierarchy.getLeastUpperBound(classBSymbol, classASymbol)).thenReturn(classASymbol);

        this.testWelltypedExpression(classASymbol, testExpression, initialScope);
    }

    @Test
    public void testWelltypedIfStatementUnrelatedType() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.ifStatement(factory.boolConst(true), factory.varRef("x"),
                factory.varRef("y"));

        final VariablesScope initialScope = Mockito.mock(VariablesScope.class);
        final IdSymbol xSymbol = IdTable.getInstance().addString("x");
        final IdSymbol classASymbol = IdTable.getInstance().addString("ClassA");
        Mockito.when(initialScope.getVariableType(xSymbol)).thenReturn(ExpressionType.create(classASymbol));
        Mockito.when(initialScope.containsVariable(xSymbol)).thenReturn(true);

        final IdSymbol ySymbol = IdTable.getInstance().addString("y");
        final IdSymbol classBSymbol = IdTable.getInstance().addString("ClassB");
        Mockito.when(initialScope.getVariableType(ySymbol)).thenReturn(ExpressionType.create(classBSymbol));
        Mockito.when(initialScope.containsVariable(ySymbol)).thenReturn(true);

        final IdSymbol objectSymbol = IdTable.getInstance().getObjectSymbol();

        Mockito.when(this.hierarchy.getLeastUpperBound(classASymbol, classBSymbol)).thenReturn(objectSymbol);
        Mockito.when(this.hierarchy.getLeastUpperBound(classBSymbol, classASymbol)).thenReturn(objectSymbol);

        this.testWelltypedExpression(objectSymbol, testExpression, initialScope);
    }

    @Test
    public void testIlltypedIfStatement() {
        final ASTFactory factory = new ASTFactory();
        final If testExpression = factory.ifStatement(factory.intConst(2), factory.boolConst(true),
                factory.boolConst(false));

        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();
        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();

        final VariablesScope initialScope = Mockito.mock(VariablesScope.class);
        final SemanticErrorReporter err = this.testIlltypedExpression(boolSymbol, testExpression, initialScope);

        Mockito.verify(err).reportTypeMismatch(testExpression.getCondition(), intSymbol, boolSymbol);
        Mockito.verifyNoMoreInteractions(err);
    }

    @Test
    public void testWelltypedLoop() {
        final ASTFactory factory = new ASTFactory();
        final Loop testExpression = factory.loop(factory.boolConst(true), factory.intConst(3));

        final IdSymbol objectSymbol = IdTable.getInstance().getObjectSymbol();

        this.testWelltypedVariableFreeExpression(objectSymbol, testExpression);
    }

    @Test
    public void testIllTypedLoop() {
        final ASTFactory factory = new ASTFactory();
        final Loop testExpression = factory.loop(factory.intConst(1), factory.intConst(3));

        final IdSymbol objectSymbol = IdTable.getInstance().getObjectSymbol();
        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();
        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();

        SemanticErrorReporter err = this.testIlltypedVariableFreeExpression(objectSymbol, testExpression);
        Mockito.verify(err).reportTypeMismatch(testExpression.getCondition(), intSymbol, boolSymbol);
        Mockito.verifyNoMoreInteractions(err);
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

    private SemanticErrorReporter testIlltypedVariableFreeExpression(IdSymbol expectedType, Expression testExpression) {
        final VariablesScope initialScope = Mockito.mock(VariablesScope.class);

        return this.testIlltypedExpression(expectedType, testExpression, initialScope);
    }

    /**
     * @return A mock of SemanticErrorReporter used during typechecking of testExpression.
     */
    private SemanticErrorReporter testIlltypedExpression(IdSymbol expectedType, Expression testExpression,
            VariablesScope initialScope) {
        final IdSymbol classId = IdTable.getInstance().addString("TestClass");

        final Map<IdSymbol, DefinedClassSignature> definedSignatures = new HashMap<>();

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);

        final ExpressionTypeChecker checker = new ExpressionTypeChecker(classId, initialScope, hierarchy,
                definedSignatures, err);
        testExpression.acceptVisitor(checker);

        Assert.assertEquals(expectedType, checker.getResultType().getTypeId(classId));

        return err;
    }
}
