package net.alexweinert.coolc.processors.coolfrontend.semantic_check;

import java.util.HashMap;
import java.util.Map;

import net.alexweinert.coolc.processors.coolfrontend.semantic_check.OverridingChecker;
import net.alexweinert.coolc.processors.coolfrontend.semantic_check.SemanticErrorReporter;
import net.alexweinert.coolc.representations.cool.ast.ASTFactory;
import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Method;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.information.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.information.ClassHierarchyFactory;
import net.alexweinert.coolc.representations.cool.information.DeclaredClassSignature;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class OverridingCheckerTest {

    @Test
    public void testNoOverridingMethods() {
        final ASTFactory factory = new ASTFactory();

        final ClassNode classOne = factory.classNode("ClassOne", "Object", factory.method("methodOne", "String"));
        final ClassNode classTwo = factory.classNode("ClassTwo", "ClassOne", factory.method("methodTwo", "Int"));

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
    public void testNoOverridingAttributes() {
        final ASTFactory factory = new ASTFactory();

        final ClassNode classOne = factory.classNode("ClassOne", "Object", factory.attribute("attributeOne", "String"));
        final ClassNode classTwo = factory.classNode("ClassTwo", "ClassOne", factory.attribute("attributeTwo", "Int"));

        final Program testProgram = factory.program(classOne, classTwo);

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);

        // TODO: Mock hierarchy, declaredSignatures
        final ClassHierarchy hierarchy = ClassHierarchyFactory.buildHierarchy(testProgram);
        final Map<IdSymbol, DeclaredClassSignature> declaredSignatures = new HashMap<>();
        declaredSignatures.put(classOne.getIdentifier(), DeclaredClassSignature.create(classOne));
        declaredSignatures.put(classTwo.getIdentifier(), DeclaredClassSignature.create(classTwo));
        declaredSignatures.put(IdTable.getInstance().getObjectSymbol(), DeclaredClassSignature.createObjectSignature());

        final Program receivedProgram = OverridingChecker.checkOverriding(testProgram, hierarchy, declaredSignatures,
                err);

        Assert.assertEquals(testProgram, receivedProgram);
        Mockito.verifyZeroInteractions(err);
    }

    @Test
    public void testCorrectOverriding() {
        final ASTFactory factory = new ASTFactory();

        final ClassNode classOne = factory.classNode("ClassOne", "Object", factory.method("methodOne", "String"));
        final ClassNode classTwo = factory.classNode("ClassTwo", "ClassOne", factory.method("methodOne", "String"));

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
        final ClassNode classOne = factory.classNode("ClassOne", "Object", methodDefOne);
        final Method methodDefTwo = factory.method("methodOne", "Int");
        final ClassNode classTwo = factory.classNode("ClassTwo", "ClassOne", methodDefTwo);

        final Program testProgram = factory.program(classOne, classTwo);

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);

        // TODO: Mock hierarchy, declaredSignatures
        final ClassHierarchy hierarchy = ClassHierarchyFactory.buildHierarchy(testProgram);
        final Map<IdSymbol, DeclaredClassSignature> declaredSignatures = new HashMap<>();
        declaredSignatures.put(classOne.getIdentifier(), DeclaredClassSignature.create(classOne));
        declaredSignatures.put(classTwo.getIdentifier(), DeclaredClassSignature.create(classTwo));
        final Program receivedProgram = OverridingChecker.checkOverriding(testProgram, hierarchy, declaredSignatures,
                err);

        final Program expectedProgram = factory.program(classOne, factory.classNode("ClassTwo", "ClassOne"));

        Assert.assertEquals(expectedProgram, receivedProgram);
        Mockito.verify(err).reportWronglyOverriddenMethod(methodDefOne, methodDefTwo);
        Mockito.verifyNoMoreInteractions(err);
    }

}
