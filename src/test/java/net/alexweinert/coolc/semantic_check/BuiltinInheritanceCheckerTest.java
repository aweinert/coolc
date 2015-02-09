package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.program.ast.ASTFactory;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.ast.Class;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class BuiltinInheritanceCheckerTest {

    @Test
    public void testObjectInheritance() {
        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);

        final ASTFactory factory = new ASTFactory("test.cl");

        final Program testProgram = factory.program(factory.classNode("Class", "Object"));

        final Program resultProgram = BuiltinInheritanceChecker.checkBuiltinInheritance(testProgram, err);

        Assert.assertEquals(testProgram, resultProgram);

        Mockito.verifyZeroInteractions(err);
    }

    @Test
    public void testBoolInheritance() {
        this.testBuiltinInheritance("Bool");
    }

    @Test
    public void testIntInheritance() {
        this.testBuiltinInheritance("Int");
    }

    @Test
    public void testStringInheritance() {
        this.testBuiltinInheritance("String");
    }

    @Test
    public void testIOInheritance() {
        this.testBuiltinInheritance("IO");
    }

    private void testBuiltinInheritance(String builtinId) {
        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);

        final ASTFactory factory = new ASTFactory("test.cl");

        final Class builtinInheritedClass = factory.classNode("ClassOne", builtinId);
        final Class otherClass = factory.classNode("ClassTwo", "Object");

        final Program testProgram = factory.program(builtinInheritedClass, otherClass);

        final Program resultProgram = BuiltinInheritanceChecker.checkBuiltinInheritance(testProgram, err);

        final Program expectedProgram = factory.program(otherClass, factory.classNode("ClassOne", "Object"));

        Assert.assertEquals(expectedProgram, resultProgram);

        Mockito.verify(err).reportBaseClassInheritance(builtinInheritedClass);
        Mockito.verifyNoMoreInteractions(err);
    }

}
