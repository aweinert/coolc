package net.alexweinert.coolc.semantic_check;

import java.util.Arrays;

import net.alexweinert.coolc.program.ast.ASTFactory;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Classes;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class BuiltinRedefinitionRemoverTest {

    @Test
    public void testObjectRemoval() {
        this.testBuiltinRemoval("Object");
    }

    @Test
    public void testBoolRemoval() {
        this.testBuiltinRemoval("Bool");
    }

    @Test
    public void testIntRemoval() {
        this.testBuiltinRemoval("Int");
    }

    @Test
    public void testStringRemoval() {
        this.testBuiltinRemoval("String");
    }

    @Test
    public void testIORemoval() {
        this.testBuiltinRemoval("IO");
    }

    @Test
    public void testNonRemoval() {
        final ISemanticErrorReporter err = Mockito.mock(ISemanticErrorReporter.class);

        final ASTFactory factory = new ASTFactory();

        final Class classOne = factory.classNode("ClassOne", "Object");
        final Class classTwo = factory.classNode("ClassTwo", "Object");
        final Program testProgram = factory.program(classOne, classTwo);

        final Program resultProgram = BuiltinRedefinitionRemover.removeBuiltinRedefinition(testProgram, err);

        Assert.assertEquals(testProgram, resultProgram);

        Mockito.verifyZeroInteractions(err);
    }

    private void testBuiltinRemoval(String builtinIdString) {
        final ISemanticErrorReporter err = Mockito.mock(ISemanticErrorReporter.class);
        final ASTFactory factory = new ASTFactory();

        final Class intRedefinition = factory.classNode(builtinIdString, "Object");
        final Class otherClass = factory.classNode("Other", "Object");

        final Program testProgram = new Program("test.cl", 1, new Classes("test.cl", 1, Arrays.asList(intRedefinition,
                otherClass)));

        final Program resultProgram = BuiltinRedefinitionRemover.removeBuiltinRedefinition(testProgram, err);

        final IdSymbol builtinId = IdTable.getInstance().addString(builtinIdString);
        final IdSymbol otherSymbol = IdTable.getInstance().addString("Other");

        Assert.assertNull(resultProgram.getClass(builtinId));
        Assert.assertEquals(otherClass, resultProgram.getClass(otherSymbol));

        Mockito.verify(err).reportRedefinitionOfBuiltInClass(builtinId, intRedefinition);
        Mockito.verifyNoMoreInteractions(err);
    }
}