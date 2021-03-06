package net.alexweinert.coolc.processors.cool.hierarchycheck;

import java.util.Collections;

import net.alexweinert.coolc.representations.cool.ast.ASTFactory;
import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Feature;
import net.alexweinert.coolc.representations.cool.ast.Features;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

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

        final Program receivedProgram = new MultipleClassesRemover(err).removeMultipleClassDefinitions(testProgram);

        Assert.assertEquals(receivedProgram, testProgram);
        Mockito.verifyZeroInteractions(err);
    }

    @Test
    public void testRedefinitionRemoval() {
        final IdSymbol classOneSymbol = IdTable.getInstance().addString("ClassOne");
        final IdSymbol objectSymbol = IdTable.getInstance().getObjectSymbol();
        final ClassNode definitionOne = new ClassNode("", 1, classOneSymbol, objectSymbol, new Features("", 1,
                Collections.<Feature> emptyList()));
        final ClassNode definitionTwo = new ClassNode("", 2, classOneSymbol, objectSymbol, new Features("", 2,
                Collections.<Feature> emptyList()));

        final ASTFactory factory = new ASTFactory();
        final Program testProgram = factory.program(definitionOne, definitionTwo);

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);

        final Program expectedProgram = factory.program(definitionOne);
        final Program receivedProgram = new MultipleClassesRemover(err).removeMultipleClassDefinitions(testProgram);

        Assert.assertEquals(expectedProgram, receivedProgram);
        Mockito.verify(err).reportClassRedefinition(definitionOne, definitionTwo);
        Mockito.verifyNoMoreInteractions(err);
    }
}
