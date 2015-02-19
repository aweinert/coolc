package net.alexweinert.coolc.semantic_check;

import java.util.HashMap;
import java.util.Map;

import net.alexweinert.coolc.program.ast.ASTFactory;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.information.ClassHierarchy;
import net.alexweinert.coolc.program.information.ClassHierarchyFactory;
import net.alexweinert.coolc.program.information.DeclaredClassSignature;
import net.alexweinert.coolc.program.symboltables.IdSymbol;

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

        // TODO: Mock hierarchy, declaredSignatures
        final ClassHierarchy hierarchy = ClassHierarchyFactory.buildHierarchy(testProgram);
        final Map<IdSymbol, DeclaredClassSignature> declaredSignatures = new HashMap<>();
        declaredSignatures.put(classOne.getIdentifier(), DeclaredClassSignature.create(classOne));
        declaredSignatures.put(classTwo.getIdentifier(), DeclaredClassSignature.create(classTwo));

        final Program receivedProgram = OverridingChecker.checkOverriding(testProgram, hierarchy, declaredSignatures,
                err);

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

        // TODO: Mock hierarchy, declaredSignatures
        final ClassHierarchy hierarchy = ClassHierarchyFactory.buildHierarchy(testProgram);
        final Map<IdSymbol, DeclaredClassSignature> declaredSignatures = new HashMap<>();
        declaredSignatures.put(classOne.getIdentifier(), DeclaredClassSignature.create(classOne));
        declaredSignatures.put(classTwo.getIdentifier(), DeclaredClassSignature.create(classTwo));
        final Program receivedProgram = OverridingChecker.checkOverriding(testProgram, hierarchy, declaredSignatures,
                err);

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

        // TODO: Mock hierarchy, declaredSignatures
        final ClassHierarchy hierarchy = ClassHierarchyFactory.buildHierarchy(testProgram);
        final Map<IdSymbol, DeclaredClassSignature> declaredSignatures = new HashMap<>();
        declaredSignatures.put(classOne.getIdentifier(), DeclaredClassSignature.create(classOne));
        declaredSignatures.put(classTwo.getIdentifier(), DeclaredClassSignature.create(classTwo));
        final Program receivedProgram = OverridingChecker.checkOverriding(testProgram, hierarchy, declaredSignatures,
                err);

        Assert.assertEquals(testProgram, receivedProgram);
        Mockito.verify(err).reportWronglyOverriddenMethod(methodDefOne, methodDefTwo);
        Mockito.verifyNoMoreInteractions(err);
    }

}
