package net.alexweinert.coolc.semantic_check;

import java.util.Collections;

import net.alexweinert.coolc.program.ast.ASTFactory;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Feature;
import net.alexweinert.coolc.program.ast.Features;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class MultipleClassesRemoverTest {

    @Test
    public void testNonRemoval() {
        final ASTFactory factory = new ASTFactory();
        final Program testProgram = factory.program(factory.classNode("ClassOne", "Object"),
                factory.classNode("ClassTwo", "Object"));

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);

        final Program receivedProgram = MultipleClassesRemover.removeMultipleClassDefinitions(testProgram, err);

        Assert.assertEquals(receivedProgram, testProgram);
        Mockito.verifyZeroInteractions(err);
    }

    @Test
    public void testRedefinitionRemoval() {
        final IdSymbol classOneSymbol = IdTable.getInstance().addString("ClassOne");
        final IdSymbol objectSymbol = IdTable.getInstance().addString("Object");
        final Class definitionOne = new Class("", 1, classOneSymbol, objectSymbol, new Features("", 1,
                Collections.<Feature> emptyList()));
        final Class definitionTwo = new Class("", 2, classOneSymbol, objectSymbol, new Features("", 2,
                Collections.<Feature> emptyList()));

        final ASTFactory factory = new ASTFactory();
        final Program testProgram = factory.program(definitionOne, definitionTwo);

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);

        final Program expectedProgram = factory.program(definitionOne);
        final Program receivedProgram = MultipleClassesRemover.removeMultipleClassDefinitions(testProgram, err);

        Assert.assertEquals(expectedProgram, receivedProgram);
        Mockito.verify(err).reportClassRedefinition(definitionOne, definitionTwo);
        Mockito.verifyNoMoreInteractions(err);
    }
}
