package net.alexweinert.coolc.semantic_check;

import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.program.ast.ASTFactory;
import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Formal;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.ast.Program;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class InterfaceCheckerTest {

    @Test
    public void testDoubleAttributeRemoval() {
        final ASTFactory factory = new ASTFactory();

        factory.setLineNumber(2);
        final Attribute attributeDefOne = factory.attribute("attributeOne", "String");
        factory.setLineNumber(3);
        final Attribute attributeDefTwo = factory.attribute("attributeOne", "String");

        factory.setLineNumber(1);
        final Class containingClass = factory.classNode("Class", "Object", attributeDefOne, attributeDefTwo);

        final Program testProgram = factory.program(containingClass);

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);
        final Program receivedProgram = InterfaceChecker.checkInterfaces(testProgram, err);

        final Program expectedProgram = factory.program(factory.classNode("Class", "Object", attributeDefOne));

        Assert.assertEquals(expectedProgram, receivedProgram);
        final List<Attribute> offendingAttributes = new LinkedList<>();
        offendingAttributes.add(attributeDefOne);
        offendingAttributes.add(attributeDefTwo);
        Mockito.verify(err).reportMultipleAttributes(containingClass, offendingAttributes);
        Mockito.verifyNoMoreInteractions(err);
    }

    @Test
    public void testDoubleMethodSameInterfaceRemoval() {
        final ASTFactory factory = new ASTFactory();

        factory.setLineNumber(2);
        final Formal formalOne = factory.formal("parameterOne", "String");
        final Method methodDefOne = factory.method("methodOne", "String", formalOne);
        factory.setLineNumber(3);
        final Method methodDefTwo = factory.method("methodOne", "String", formalOne);

        factory.setLineNumber(1);
        final Class containingClass = factory.classNode("Class", "Object", methodDefOne, methodDefTwo);

        final Program testProgram = factory.program(containingClass);

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);
        final Program receivedProgram = InterfaceChecker.checkInterfaces(testProgram, err);

        final Program expectedProgram = factory.program(factory.classNode("Class", "Object", methodDefOne));

        Assert.assertEquals(expectedProgram, receivedProgram);
        final List<Method> offendingMethods = new LinkedList<>();
        offendingMethods.add(methodDefOne);
        offendingMethods.add(methodDefTwo);
        Mockito.verify(err).reportMultipleMethods(containingClass, offendingMethods);
        Mockito.verifyNoMoreInteractions(err);
    }

    @Test
    public void testDoubleMethodDifferentInterfaceRemoval() {
        final ASTFactory factory = new ASTFactory();

        factory.setLineNumber(2);
        final Formal formalOne = factory.formal("parameterOne", "String");
        final Method methodDefOne = factory.method("methodOne", "String", formalOne);
        factory.setLineNumber(3);
        final Formal formalTwo = factory.formal("parameterOne", "Int");
        final Method methodDefTwo = factory.method("methodOne", "Int", formalOne);

        factory.setLineNumber(1);
        final Class containingClass = factory.classNode("Class", "Object", methodDefOne, methodDefTwo);

        final Program testProgram = factory.program(containingClass);

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);
        final Program receivedProgram = InterfaceChecker.checkInterfaces(testProgram, err);

        final Program expectedProgram = factory.program(factory.classNode("Class", "Object", methodDefOne));

        Assert.assertEquals(expectedProgram, receivedProgram);
        final List<Method> offendingMethods = new LinkedList<>();
        offendingMethods.add(methodDefOne);
        offendingMethods.add(methodDefTwo);
        Mockito.verify(err).reportMultipleMethods(containingClass, offendingMethods);
        Mockito.verifyNoMoreInteractions(err);
    }
}
