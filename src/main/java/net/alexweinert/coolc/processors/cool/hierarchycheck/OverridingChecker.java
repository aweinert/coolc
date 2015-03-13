package net.alexweinert.coolc.processors.cool.hierarchycheck;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.representations.cool.ast.Visitor;
import net.alexweinert.coolc.representations.cool.ast.Attribute;
import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Feature;
import net.alexweinert.coolc.representations.cool.ast.Method;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.information.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.information.DeclaredClassSignature;
import net.alexweinert.coolc.representations.cool.information.MethodSignature;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

class OverridingChecker extends Visitor {
    /**
     * Checks that no class overrides its parent's attributes and that each class only overrides its parent's methods in
     * the allowed way (i.e., argument and return types match)
     */
    public static Program checkOverriding(Program program, SemanticErrorReporter err) {
        final OverridingChecker checker = new OverridingChecker(err);
        program.acceptVisitor(checker);
        return program;
    }

    final private SemanticErrorReporter err;

    private Program containingProgram;
    private ClassNode containingClass;

    private OverridingChecker(SemanticErrorReporter err) {
        this.err = err;
    }

    @Override
    public void visitProgramPreorder(Program program) {
        this.containingProgram = program;
    }

    @Override
    public void visitProgramPostorder(Program program) {
        this.containingProgram = null;
    }

    @Override
    public void visitClassPreorder(ClassNode classNode) {
        this.containingClass = classNode;
    }

    @Override
    public void visitClassPostorder(ClassNode classNode) {
        this.containingClass = classNode;
    }

    @Override
    public void visitAttributePostorder(Attribute attribute) {
        ClassNode currentClass = this.containingClass;
        while (!currentClass.getParent().equals(IdTable.getInstance().getObjectSymbol())) {
            currentClass = this.containingProgram.getClass(currentClass.getParent());
            final Attribute ancestorAttribute = currentClass.getAttribute(attribute.getName());
            if (ancestorAttribute != null) {
                err.reportOverriddenAttribute(ancestorAttribute, attribute);
            }
        }
    }

    @Override
    public void visitMethodPostorder(Method method) {
        ClassNode currentClass = this.containingClass;
        while (!currentClass.getParent().equals(IdTable.getInstance().getObjectSymbol())) {
            currentClass = this.containingProgram.getClass(currentClass.getParent());
            final Method ancestorMethod = currentClass.getMethod(method.getName());
            if (ancestorMethod != null && !(method.getSignature().equals(ancestorMethod.getSignature()))) {
                err.reportWronglyOverriddenMethod(ancestorMethod, method);
            }
        }
    }
}
