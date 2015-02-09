package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.program.ast.ASTFactory;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Program;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ParentDefinednessCheckerTest {

    @Test
    public void testCheckParentDefinedness() {
        final ASTFactory factory = new ASTFactory();
        final Class undefParentClass = factory.classNode("ClassOne", "UndefClass");
        final Class defParentClass = factory.classNode("ClassTwo", "Object");

        final Program testProgram = factory.program(undefParentClass, defParentClass);

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);

        final Program expectedProgram = factory.program(factory.classNode("ClassOne", "Object"), defParentClass);
        final Program receivedProgram = ParentDefinednessChecker.checkParentDefinedness(testProgram, err);

        Mockito.verify(err).reportUndefinedParentClass(undefParentClass);
        Mockito.verifyNoMoreInteractions(err);

        Assert.assertEquals(expectedProgram, receivedProgram);
    }
}
