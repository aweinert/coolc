package net.alexweinert.coolc.processors.cool.hierarchycheck;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.alexweinert.coolc.representations.cool.ast.ASTFactory;
import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CircularInheritanceRemoverTest {

    @Test
    public void testOneCircleInheritanceRemoval() {
        final ASTFactory factory = new ASTFactory();
        final ClassNode classOne = factory.classNode("ClassOne", "ClassOne");
        final ClassNode classTwo = factory.classNode("ClassTwo", "Object");

        final Program testProgram = factory.program(classOne, classTwo);

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);
        final Program receivedProgram = CircularInheritanceRemover.removeCircularInheritance(testProgram, err);

        final Set<ClassNode> circularClasses = new HashSet<>(Arrays.asList(classOne));

        Mockito.verify(err).reportCircularInheritance(circularClasses, classOne);
        Mockito.verifyNoMoreInteractions(err);

        final ClassNode receivedClassOne = receivedProgram.getClass(classOne.getIdentifier());
        final ClassNode receivedClassTwo = receivedProgram.getClass(classTwo.getIdentifier());

        // Make sure that no class was removed
        Assert.assertNotNull(receivedClassOne);
        Assert.assertNotNull(receivedClassTwo);

        // Assert that ClassOne now inherits from Object
        final IdSymbol objectSymbol = IdTable.getInstance().getObjectSymbol();
        final boolean classOneInheritsObject = receivedClassOne.getParent().equals(objectSymbol);
        Assert.assertTrue(classOneInheritsObject);

        // Make sure that the second class was not touched
        Assert.assertEquals(classTwo, receivedClassTwo);
    }

    @Test
    public void testTwoCircleInheritanceRemoval() {
        final ASTFactory factory = new ASTFactory();
        final ClassNode classOne = factory.classNode("ClassOne", "ClassTwo");
        final ClassNode classTwo = factory.classNode("ClassTwo", "ClassOne");
        final ClassNode classThree = factory.classNode("ClassThree", "Object");

        final Program testProgram = factory.program(classOne, classTwo, classThree);

        final SemanticErrorReporter err = Mockito.mock(SemanticErrorReporter.class);
        final Program receivedProgram = CircularInheritanceRemover.removeCircularInheritance(testProgram, err);

        final Set<ClassNode> circularClasses = new HashSet<>(Arrays.asList(classOne, classTwo));

        Mockito.verify(err).reportCircularInheritance(Mockito.eq(circularClasses), Mockito.any(ClassNode.class));
        Mockito.verifyNoMoreInteractions(err);

        final ClassNode receivedClassOne = receivedProgram.getClass(classOne.getIdentifier());
        final ClassNode receivedClassTwo = receivedProgram.getClass(classTwo.getIdentifier());
        final ClassNode receivedClassThree = receivedProgram.getClass(classThree.getIdentifier());

        // Make sure that no class was removed
        Assert.assertNotNull(receivedClassOne);
        Assert.assertNotNull(receivedClassTwo);
        Assert.assertNotNull(receivedClassThree);

        // Make sure that only one class out of classOne and classTwo has had its parent set to Object
        final IdSymbol objectSymbol = IdTable.getInstance().getObjectSymbol();
        final boolean classOneInheritsObject = receivedClassOne.getParent().equals(objectSymbol);
        final boolean classTwoInheritsObject = receivedClassTwo.getParent().equals(objectSymbol);
        final boolean classOneInheritsClassTwo = receivedClassOne.getParent().equals(classTwo.getIdentifier());
        final boolean classTwoInheritsClassOne = receivedClassTwo.getParent().equals(classOne.getIdentifier());

        Assert.assertTrue(classOneInheritsObject || classTwoInheritsObject);
        Assert.assertTrue(classOneInheritsClassTwo || classTwoInheritsClassOne);

        // Make sure that the third class was not touched
        Assert.assertEquals(classThree, receivedClassThree);
    }
}
