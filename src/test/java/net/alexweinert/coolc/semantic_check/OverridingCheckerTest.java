package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.program.ast.ASTFactory;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.ast.Program;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class OverridingCheckerTest {

    @Test
    public void testNoOverriding() {
        final ASTFactory factory = new ASTFactory();

        final Class classOne = factory.classNode("ClassOne", "Object", factory.method("methodOne", "String"));
        final Class classTwo = factory.classNode("ClassTwo", "ClassOne", factory.method("methodTwo", "Int"));

        final Program testProgram = factory.program(classOne, classTwo);

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);

        final Program receivedProgram = OverridingChecker.checkOverriding(testProgram, err);

        Assert.assertEquals(testProgram, receivedProgram);
        Mockito.verifyZeroInteractions(err);
    }

    @Test
    public void testCorrectOverriding() {
        final ASTFactory factory = new ASTFactory();

        final Class classOne = factory.classNode("ClassOne", "Object", factory.method("methodOne", "String"));
        final Class classTwo = factory.classNode("ClassTwo", "ClassOne", factory.method("methodOne", "String"));

        final Program testProgram = factory.program(classOne, classTwo);

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);

        final Program receivedProgram = OverridingChecker.checkOverriding(testProgram, err);

        Assert.assertEquals(testProgram, receivedProgram);
        Mockito.verifyZeroInteractions(err);
    }

    @Test
    public void testIncorrectOverriding() {
        final ASTFactory factory = new ASTFactory();

        final Method methodDefOne = factory.method("methodOne", "String");
        final Class classOne = factory.classNode("ClassOne", "Object", methodDefOne);
        final Method methodDefTwo = factory.method("methodOne", "Int");
        final Class classTwo = factory.classNode("ClassTwo", "ClassOne", methodDefTwo);

        final Program testProgram = factory.program(classOne, classTwo);

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);

        final Program receivedProgram = OverridingChecker.checkOverriding(testProgram, err);

        final Program expectedProgram = factory.program(classOne, factory.classNode("ClassTwo", "ClassOne"));

        Assert.assertEquals(expectedProgram, receivedProgram);
        Mockito.verify(err).reportWronglyOverriddenMethod(methodDefOne, methodDefTwo);
        Mockito.verifyNoMoreInteractions(err);
    }

}
