package net.alexweinert.coolc.semantic_check;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Classes;
import net.alexweinert.coolc.program.ast.Feature;
import net.alexweinert.coolc.program.ast.Features;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.information.ClassHierarchy;
import net.alexweinert.coolc.program.information.ClassHierarchyFactory;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

class OverridingChecker extends ASTVisitor {

    final private ClassHierarchy hierarchy;
    final private SemanticErrorReporter error;

    final private List<Attribute> attributes = new LinkedList<>();
    final private List<Method> methods = new LinkedList<>();
    final private List<Class> classes = new LinkedList<>();
    private Program containingProgram;
    private Program resultingProgram;
    private List<IdSymbol> ancestorsDescendingOrder;

    OverridingChecker(ClassHierarchy hierarchy, SemanticErrorReporter error) {
        this.hierarchy = hierarchy;
        this.error = error;
    }

    private static Program checkInheritance(Program program, ClassHierarchy hierarchy, SemanticErrorReporter error) {
        final OverridingChecker checker = new OverridingChecker(hierarchy, error);
        program.acceptVisitor(checker);
        return checker.resultingProgram;
    }

    @Override
    public void visitAttributePostorder(Attribute attribute) {
        for (IdSymbol ancestor : this.ancestorsDescendingOrder) {
            final Attribute existingAttribute = this.containingProgram.getClass(ancestor).getAttribute(
                    attribute.getName());
            if (existingAttribute != null) {
                this.error.reportOverriddenAttribute(attribute, existingAttribute);
                return;
            }
        }
        this.attributes.add(attribute);
    }

    @Override
    public void visitMethodPostorder(Method method) {
        for (IdSymbol ancestor : this.ancestorsDescendingOrder) {
            if (!ancestor.equals(IdTable.getInstance().getObjectSymbol())) {
                final Method existingMethod = this.containingProgram.getClass(ancestor).getMethod(method.getName());
                if (existingMethod != null) {
                    this.error.reportWronglyOverriddenMethod(method, existingMethod);
                    return;
                }
            }
        }
        this.methods.add(method);
    }

    @Override
    public void visitClassPreorder(Class classNode) {
        final List<IdSymbol> ancestors = this.hierarchy.getAncestors(classNode.getIdentifier());
        Collections.reverse(ancestors);
        this.ancestorsDescendingOrder = ancestors.subList(0, ancestors.size() - 2);
    }

    @Override
    public void visitClassPostorder(Class classNode) {
        final List<Feature> features = new LinkedList<>();
        features.addAll(this.attributes);
        features.addAll(this.methods);
        this.attributes.clear();
        this.methods.clear();
        final Features featuresNode = new Features(classNode.getFilename(), classNode.getLineNumber(), features);
        final Class newClass = new Class(classNode.getFilename(), classNode.getLineNumber(), classNode.getIdentifier(),
                classNode.getParent(), featuresNode);
        this.classes.add(newClass);
        this.ancestorsDescendingOrder = null;
    }

    @Override
    public void visitProgramPreorder(Program program) {
        this.containingProgram = program;
    }

    @Override
    public void visitProgramPostorder(Program program) {
        final Classes newClasses = new Classes(program.getFilename(), program.getLineNumber(), this.classes);
        this.resultingProgram = new Program(program.getFilename(), program.getLineNumber(), newClasses);
    }

    /**
     * Checks that no class overrides its parent's attributes and that each class only overrides its parent's methods in
     * the allowed way (i.e., argument and return types match)
     */
    public static Program checkOverriding(Program program, SemanticErrorReporter err) {
        final ClassHierarchy hierarchy = ClassHierarchyFactory.buildHierarchy(program);
        return OverridingChecker.checkInheritance(program, hierarchy, err);
    }

}
