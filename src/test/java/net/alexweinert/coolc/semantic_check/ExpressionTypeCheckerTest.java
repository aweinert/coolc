package net.alexweinert.coolc.semantic_check;

import java.util.HashMap;
import java.util.Map;

import net.alexweinert.coolc.program.ast.ASTFactory;
import net.alexweinert.coolc.program.ast.Expression;
import net.alexweinert.coolc.program.information.ClassHierarchy;
import net.alexweinert.coolc.program.information.DefinedClassSignature;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;

public class ExpressionTypeCheckerTest {

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

    private void testWelltypedVariableFreeExpression(IdSymbol expectedType, Expression testExpression) {
        final IdSymbol classId = IdTable.getInstance().addString("TestClass");
        final VariablesScope initialScope = Mockito.mock(VariablesScope.class);

        final ClassHierarchy hierarchy = Mockito.mock(ClassHierarchy.class);
        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();
        Mockito.when(hierarchy.conformsTo(intSymbol, intSymbol)).thenReturn(true);

        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();
        Mockito.when(hierarchy.conformsTo(boolSymbol, boolSymbol)).thenReturn(true);

        final IdSymbol stringSymbol = IdTable.getInstance().getStringSymbol();
        Mockito.when(hierarchy.conformsTo(stringSymbol, stringSymbol)).thenReturn(true);

        final Map<IdSymbol, DefinedClassSignature> definedSignatures = new HashMap<>();

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);

        final ExpressionTypeChecker checker = new ExpressionTypeChecker(classId, initialScope, hierarchy,
                definedSignatures, err);
        testExpression.acceptVisitor(checker);

        Assert.assertEquals(expectedType, checker.getResultType().getTypeId(classId));

        Mockito.verifyZeroInteractions(err);
    }

}
