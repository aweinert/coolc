package net.alexweinert.coolc.processors.cool.typecheck;

import java.util.HashMap;
import java.util.Map;

import net.alexweinert.coolc.processors.cool.typecheck.ExpressionType;
import net.alexweinert.coolc.processors.cool.typecheck.ExpressionTypeChecker;
import net.alexweinert.coolc.processors.cool.typecheck.TypeErrorReporter;
import net.alexweinert.coolc.processors.cool.typecheck.VariablesScope;
import net.alexweinert.coolc.representations.cool.ast.ASTFactory;
import net.alexweinert.coolc.representations.cool.ast.Expression;
import net.alexweinert.coolc.representations.cool.ast.FunctionCall;
import net.alexweinert.coolc.representations.cool.ast.If;
import net.alexweinert.coolc.representations.cool.ast.Let;
import net.alexweinert.coolc.representations.cool.ast.Loop;
import net.alexweinert.coolc.representations.cool.ast.ObjectReference;
import net.alexweinert.coolc.representations.cool.information.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.information.DefinedClassSignature;
import net.alexweinert.coolc.representations.cool.information.MethodSignature;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ExpressionTypeCheckerTest {

    ClassHierarchy hierarchy;

    private final IdSymbol testClassSymbol = IdTable.getInstance().addString("TestClass");

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
        Mockito.when(scope.getVariableType(variableId)).thenReturn(
                ExpressionType.create(boolSymbol, this.testClassSymbol));
        Mockito.when(scope.containsVariable(variableId)).thenReturn(true);

        this.testWelltypedExpression(boolSymbol, testExpression, scope);
    }

    @Test
    public void testObjectReferenceOutOfScope() {
        final ASTFactory factory = new ASTFactory();
        final ObjectReference testExpression = factory.varRef("foo");

        final VariablesScope scope = Mockito.mock(VariablesScope.class);

        final IdSymbol objectSymbol = IdTable.getInstance().getObjectSymbol();

        final TypeErrorReporter err = this.testIlltypedExpression(objectSymbol, testExpression, scope);

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
                ExpressionType.create(intSymbol, this.testClassSymbol));

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
        Mockito.when(scope.getVariableType(xSymbol)).thenReturn(
                ExpressionType.create(classASymbol, this.testClassSymbol));

        final IdSymbol ySymbol = IdTable.getInstance().addString("y");
        Mockito.when(scope.containsVariable(ySymbol)).thenReturn(true);
        Mockito.when(scope.getVariableType(ySymbol)).thenReturn(
                ExpressionType.create(classBSymbol, this.testClassSymbol));

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
        Mockito.when(scope.getVariableType(xSymbol)).thenReturn(
                ExpressionType.create(classASymbol, this.testClassSymbol));

        final IdSymbol ySymbol = IdTable.getInstance().addString("y");
        Mockito.when(scope.containsVariable(ySymbol)).thenReturn(true);
        Mockito.when(scope.getVariableType(ySymbol)).thenReturn(
                ExpressionType.create(classBSymbol, this.testClassSymbol));

        Mockito.when(this.hierarchy.conformsTo(classBSymbol, classASymbol)).thenReturn(false);

        TypeErrorReporter err = this.testIlltypedExpression(classASymbol, testExpression, scope);

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
        Mockito.when(initialScope.getVariableType(xSymbol)).thenReturn(
                ExpressionType.create(classASymbol, this.testClassSymbol));
        Mockito.when(initialScope.containsVariable(xSymbol)).thenReturn(true);

        final IdSymbol ySymbol = IdTable.getInstance().addString("y");
        final IdSymbol classBSymbol = IdTable.getInstance().addString("ClassB");
        Mockito.when(initialScope.getVariableType(ySymbol)).thenReturn(
                ExpressionType.create(classBSymbol, this.testClassSymbol));
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
        Mockito.when(initialScope.getVariableType(xSymbol)).thenReturn(
                ExpressionType.create(classASymbol, this.testClassSymbol));
        Mockito.when(initialScope.containsVariable(xSymbol)).thenReturn(true);

        final IdSymbol ySymbol = IdTable.getInstance().addString("y");
        final IdSymbol classBSymbol = IdTable.getInstance().addString("ClassB");
        Mockito.when(initialScope.getVariableType(ySymbol)).thenReturn(
                ExpressionType.create(classBSymbol, this.testClassSymbol));
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
        final TypeErrorReporter err = this.testIlltypedExpression(boolSymbol, testExpression, initialScope);

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
    public void testIlltypedLoop() {
        final ASTFactory factory = new ASTFactory();
        final Loop testExpression = factory.loop(factory.intConst(1), factory.intConst(3));

        final IdSymbol objectSymbol = IdTable.getInstance().getObjectSymbol();
        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();
        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();

        TypeErrorReporter err = this.testIlltypedVariableFreeExpression(objectSymbol, testExpression);
        Mockito.verify(err).reportTypeMismatch(testExpression.getCondition(), intSymbol, boolSymbol);
        Mockito.verifyNoMoreInteractions(err);
    }

    @Test
    public void testBlockOneStatement() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.block(factory.intConst(1));

        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();
        this.testWelltypedVariableFreeExpression(intSymbol, testExpression);
    }

    @Test
    public void testBlockTwoStatements() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.block(factory.boolConst(true), factory.intConst(1));

        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();
        this.testWelltypedVariableFreeExpression(intSymbol, testExpression);
    }

    @Test
    public void testIsVoidExpression() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.isVoid(factory.intConst(3));

        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();
        this.testWelltypedVariableFreeExpression(boolSymbol, testExpression);
    }

    @Test
    public void testWelltypedLet() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.let("x", "Bool", factory.boolConst(true), factory.varRef("x"));

        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();
        final IdSymbol variableSymbol = IdTable.getInstance().addString("x");

        final VariablesScope innerScope = Mockito.mock(VariablesScope.class);
        Mockito.when(innerScope.getVariableType(variableSymbol)).thenReturn(
                ExpressionType.create(boolSymbol, this.testClassSymbol));
        Mockito.when(innerScope.containsVariable(variableSymbol)).thenReturn(true);

        final VariablesScope initialScope = Mockito.mock(VariablesScope.class);
        Mockito.when(initialScope.addVariable(this.testClassSymbol, variableSymbol, boolSymbol)).thenReturn(innerScope);

        this.testWelltypedExpression(boolSymbol, testExpression, initialScope);
    }

    @Test
    public void testIlltypedLet() {
        final ASTFactory factory = new ASTFactory();
        final Let testExpression = factory.let("x", "Bool", factory.intConst(3), factory.varRef("x"));

        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();
        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();
        final IdSymbol variableSymbol = IdTable.getInstance().addString("x");

        final VariablesScope innerScope = Mockito.mock(VariablesScope.class);
        Mockito.when(innerScope.getVariableType(variableSymbol)).thenReturn(
                ExpressionType.create(boolSymbol, this.testClassSymbol));
        Mockito.when(innerScope.containsVariable(variableSymbol)).thenReturn(true);

        final VariablesScope initialScope = Mockito.mock(VariablesScope.class);
        Mockito.when(initialScope.addVariable(this.testClassSymbol, variableSymbol, boolSymbol)).thenReturn(innerScope);

        final TypeErrorReporter err = this.testIlltypedExpression(boolSymbol, testExpression, initialScope);

        Mockito.verify(err).reportTypeMismatch(testExpression.getInitializer(), intSymbol, boolSymbol);
        Mockito.verifyNoMoreInteractions(err);
    }

    @Test
    public void testWelltypedFunctionCallFlat() {
        final ASTFactory factory = new ASTFactory();
        final FunctionCall call = factory
                .call(factory.varRef("x"), "bar", factory.intConst(2), factory.boolConst(true));

        final IdSymbol xSymbol = IdTable.getInstance().addString("x");
        final IdSymbol barSymbol = IdTable.getInstance().addString("bar");
        final IdSymbol classASymbol = IdTable.getInstance().addString("ClassA");

        final VariablesScope initialScope = Mockito.mock(VariablesScope.class);
        Mockito.when(initialScope.containsVariable(xSymbol)).thenReturn(true);
        Mockito.when(initialScope.getVariableType(xSymbol)).thenReturn(
                ExpressionType.create(classASymbol, this.testClassSymbol));

        final Map<IdSymbol, DefinedClassSignature> definedSignatures = new HashMap<>();
        final MethodSignature barSignature = MethodSignature.getFactory().create(
                factory.method("bar", "String", factory.formal("x", "Int"), factory.formal("y", "Bool")));

        final DefinedClassSignature classSignature = Mockito.mock(DefinedClassSignature.class);
        Mockito.when(classSignature.getMethodSignature(barSymbol)).thenReturn(barSignature);

        definedSignatures.put(classASymbol, classSignature);

        final IdSymbol classId = IdTable.getInstance().addString("TestClass");

        final TypeErrorReporter err = Mockito.mock(TypeErrorReporter.class);

        final ExpressionTypeChecker checker = new ExpressionTypeChecker(classId, initialScope, hierarchy,
                definedSignatures, err);
        call.acceptVisitor(checker);

        Assert.assertEquals(IdTable.getInstance().getStringSymbol(), checker.getResultType().getTypeId());

        Mockito.verifyZeroInteractions(err);
    }

    @Test
    public void testWelltypedFunctionCallNested() {
        final ASTFactory factory = new ASTFactory();
        final FunctionCall call = factory.call(factory.varRef("x"), "bar",
                factory.call(factory.varRef("x"), "bar", factory.intConst(3), factory.boolConst(true)),
                factory.boolConst(true));

        final IdSymbol xSymbol = IdTable.getInstance().addString("x");
        final IdSymbol barSymbol = IdTable.getInstance().addString("bar");
        final IdSymbol classASymbol = IdTable.getInstance().addString("ClassA");

        final VariablesScope initialScope = Mockito.mock(VariablesScope.class);
        Mockito.when(initialScope.containsVariable(xSymbol)).thenReturn(true);
        Mockito.when(initialScope.getVariableType(xSymbol)).thenReturn(
                ExpressionType.create(classASymbol, this.testClassSymbol));

        final Map<IdSymbol, DefinedClassSignature> definedSignatures = new HashMap<>();
        final MethodSignature barSignature = MethodSignature.getFactory().create(
                factory.method("bar", "Int", factory.formal("x", "Int"), factory.formal("y", "Bool")));

        final DefinedClassSignature classSignature = Mockito.mock(DefinedClassSignature.class);
        Mockito.when(classSignature.getMethodSignature(barSymbol)).thenReturn(barSignature);

        definedSignatures.put(classASymbol, classSignature);

        final IdSymbol classId = IdTable.getInstance().addString("TestClass");

        final TypeErrorReporter err = Mockito.mock(TypeErrorReporter.class);

        final ExpressionTypeChecker checker = new ExpressionTypeChecker(classId, initialScope, hierarchy,
                definedSignatures, err);
        call.acceptVisitor(checker);

        Assert.assertEquals(IdTable.getInstance().getIntSymbol(), checker.getResultType().getTypeId());

        Mockito.verifyZeroInteractions(err);
    }

    @Test
    public void testWelltypedStaticFunctionCall() {
        final ASTFactory factory = new ASTFactory();
        final FunctionCall call = factory.staticCall(factory.varRef("x"), "ClassA", "bar", factory.intConst(2),
                factory.boolConst(true));

        final IdSymbol xSymbol = IdTable.getInstance().addString("x");
        final IdSymbol barSymbol = IdTable.getInstance().addString("bar");
        final IdSymbol classASymbol = IdTable.getInstance().addString("ClassA");

        Mockito.when(this.hierarchy.conformsTo(classASymbol, classASymbol)).thenReturn(true);

        final VariablesScope initialScope = Mockito.mock(VariablesScope.class);
        Mockito.when(initialScope.containsVariable(xSymbol)).thenReturn(true);
        Mockito.when(initialScope.getVariableType(xSymbol)).thenReturn(
                ExpressionType.create(classASymbol, this.testClassSymbol));

        final Map<IdSymbol, DefinedClassSignature> definedSignatures = new HashMap<>();
        final MethodSignature barSignature = MethodSignature.getFactory().create(
                factory.method("bar", "String", factory.formal("x", "Int"), factory.formal("y", "Bool")));

        final DefinedClassSignature classSignature = Mockito.mock(DefinedClassSignature.class);
        Mockito.when(classSignature.getMethodSignature(barSymbol)).thenReturn(barSignature);

        definedSignatures.put(classASymbol, classSignature);

        final IdSymbol classId = IdTable.getInstance().addString("TestClass");

        final TypeErrorReporter err = Mockito.mock(TypeErrorReporter.class);

        final ExpressionTypeChecker checker = new ExpressionTypeChecker(classId, initialScope, hierarchy,
                definedSignatures, err);
        call.acceptVisitor(checker);

        Assert.assertEquals(IdTable.getInstance().getStringSymbol(), checker.getResultType().getTypeId());

        Mockito.verifyZeroInteractions(err);
    }

    @Test
    public void testNew() {
        final ASTFactory factory = new ASTFactory();
        final Expression testExpression = factory.newNode("Int");

        this.testWelltypedVariableFreeExpression(IdTable.getInstance().getIntSymbol(), testExpression);
    }

    private void testWelltypedVariableFreeExpression(IdSymbol expectedType, Expression testExpression) {
        final VariablesScope initialScope = Mockito.mock(VariablesScope.class);

        this.testWelltypedExpression(expectedType, testExpression, initialScope);
    }

    private void testWelltypedExpression(IdSymbol expectedType, Expression testExpression, VariablesScope initialScope) {
        final Map<IdSymbol, DefinedClassSignature> definedSignatures = new HashMap<>();

        final TypeErrorReporter err = Mockito.mock(TypeErrorReporter.class);

        final ExpressionTypeChecker checker = new ExpressionTypeChecker(this.testClassSymbol, initialScope, hierarchy,
                definedSignatures, err);
        testExpression.acceptVisitor(checker);

        Assert.assertEquals(expectedType, checker.getResultType().getTypeId());

        Mockito.verifyZeroInteractions(err);
    }

    private TypeErrorReporter testIlltypedVariableFreeExpression(IdSymbol expectedType, Expression testExpression) {
        final VariablesScope initialScope = Mockito.mock(VariablesScope.class);

        return this.testIlltypedExpression(expectedType, testExpression, initialScope);
    }

    /**
     * @return A mock of SemanticErrorReporter used during typechecking of testExpression.
     */
    private TypeErrorReporter testIlltypedExpression(IdSymbol expectedType, Expression testExpression,
            VariablesScope initialScope) {
        final Map<IdSymbol, DefinedClassSignature> definedSignatures = new HashMap<>();

        final TypeErrorReporter err = Mockito.mock(TypeErrorReporter.class);

        final ExpressionTypeChecker checker = new ExpressionTypeChecker(this.testClassSymbol, initialScope, hierarchy,
                definedSignatures, err);
        testExpression.acceptVisitor(checker);

        Assert.assertEquals(expectedType, checker.getResultType().getTypeId());

        return err;
    }
}
