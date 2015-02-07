package net.alexweinert.coolc.semantic_check;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Classes;
import net.alexweinert.coolc.program.ast.Feature;
import net.alexweinert.coolc.program.ast.Features;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

import org.junit.Test;
import org.mockito.Mockito;

public class BuiltinRedefinitionRemoverTest {

    @Test
    public void testObjectRemoval() {
        this.testBuiltinRemoval(IdTable.getInstance().addString("Object"));
    }

    @Test
    public void testBoolRemoval() {
        this.testBuiltinRemoval(IdTable.getInstance().addString("Bool"));
    }

    @Test
    public void testIntRemoval() {
        this.testBuiltinRemoval(IdTable.getInstance().addString("Int"));
    }

    @Test
    public void testStringRemoval() {
        this.testBuiltinRemoval(IdTable.getInstance().addString("String"));
    }

    @Test
    public void testIORemoval() {
        this.testBuiltinRemoval(IdTable.getInstance().addString("IO"));
    }

    @Test
    public void testNonRemoval() {
        final ISemanticErrorReporter err = Mockito.mock(ISemanticErrorReporter.class);

        final IdSymbol objectSymbol = IdTable.getInstance().addString("Object");
        final IdSymbol classOneSymbol = IdTable.getInstance().addString("ClassOne");
        final IdSymbol classTwoSymbol = IdTable.getInstance().addString("ClassTwo");
        final Class classOne = new Class("test.cl", 1, classOneSymbol, objectSymbol, new Features("test.cl", 1,
                Collections.<Feature> emptyList()));
        final Class classTwo = new Class("test.cl", 1, classTwoSymbol, objectSymbol, new Features("test.cl", 1,
                Collections.<Feature> emptyList()));

        final Program testProgram = new Program("test.cl", 1, new Classes("test.cl", 1, Arrays.asList(classOne,
                classTwo)));

        final Program resultProgram = BuiltinRedefinitionRemover.removeBuiltinRedefinition(testProgram, err);

        Assert.assertEquals(testProgram, resultProgram);

        Mockito.verifyZeroInteractions(err);
    }

    private void testBuiltinRemoval(IdSymbol builtinId) {
        final ISemanticErrorReporter err = Mockito.mock(ISemanticErrorReporter.class);

        final IdSymbol objectSymbol = IdTable.getInstance().addString("Object");
        final IdSymbol otherSymbol = IdTable.getInstance().addString("Other");
        final Class intRedefinition = new Class("test.cl", 1, builtinId, objectSymbol, new Features("test.cl", 1,
                Collections.<Feature> emptyList()));
        final Class otherClass = new Class("test.cl", 1, otherSymbol, objectSymbol, new Features("test.cl", 1,
                Collections.<Feature> emptyList()));

        final Program testProgram = new Program("test.cl", 1, new Classes("test.cl", 1, Arrays.asList(intRedefinition,
                otherClass)));

        final Program resultProgram = BuiltinRedefinitionRemover.removeBuiltinRedefinition(testProgram, err);

        Assert.assertNull(resultProgram.getClass(builtinId));
        Assert.assertEquals(otherClass, resultProgram.getClass(otherSymbol));

        Mockito.verify(err).reportRedefinitionOfBuiltInClass(builtinId, intRedefinition);
        Mockito.verifyNoMoreInteractions(err);
    }
}