package net.alexweinert.coolc.semantic_check;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Feature;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.information.ClassHierarchy;
import net.alexweinert.coolc.program.information.DeclaredClassSignature;
import net.alexweinert.coolc.program.information.MethodSignature;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

class OverridingChecker extends ASTVisitor {
    /**
     * Checks that no class overrides its parent's attributes and that each class only overrides its parent's methods in
     * the allowed way (i.e., argument and return types match)
     */
    public static Program checkOverriding(Program program, ClassHierarchy hierarchy,
            Map<IdSymbol, DeclaredClassSignature> declaredSignatures, SemanticErrorReporter err) {
        final List<Class> newClasses = new LinkedList<>();
        for (Class classNode : program.getClasses()) {
            final OverridingChecker checker = new OverridingChecker(program, classNode, hierarchy, declaredSignatures,
                    err);
            classNode.acceptVisitor(checker);
            newClasses.add(classNode.setFeatures(checker.features));
        }

        return program.setClasses(newClasses);
    }

    final private Program containingProgram;
    final private Class containingClass;
    final private ClassHierarchy classHierarchy;
    final private Map<IdSymbol, DeclaredClassSignature> declaredSignatures;
    final private SemanticErrorReporter err;

    final private List<Feature> features = new LinkedList<>();

    private OverridingChecker(Program containingProgram, Class containingClass, ClassHierarchy classHierarchy,
            Map<IdSymbol, DeclaredClassSignature> declaredSignatures, SemanticErrorReporter err) {
        this.containingProgram = containingProgram;
        this.containingClass = containingClass;
        this.classHierarchy = classHierarchy;
        this.declaredSignatures = declaredSignatures;
        this.err = err;
    }

    @Override
    public void visitAttributePostorder(Attribute attribute) {
        boolean existingAttributeFound = false;
        for (IdSymbol ancestor : this.classHierarchy.getStrictAncestors(this.containingClass.getIdentifier())) {
            if (declaredSignatures.get(ancestor).getAttribute(attribute.getName()) != null) {
                err.reportOverriddenAttribute(containingProgram.getClass(ancestor).getAttribute(attribute.getName()),
                        attribute);
                existingAttributeFound = true;
            }
        }
        if (!existingAttributeFound) {
            this.features.add(attribute);
        }
    }

    @Override
    public void visitMethodPostorder(Method method) {
        boolean existingMethodFound = false;
        for (IdSymbol ancestor : this.classHierarchy.getStrictAncestors(this.containingClass.getIdentifier())) {
            if (ancestor.equals(IdTable.getInstance().getObjectSymbol())) {
                continue;
            }
            final MethodSignature parentSignature = declaredSignatures.get(ancestor).getMethodSignature(
                    method.getName());
            if (parentSignature != null && !parentSignature.equals(MethodSignature.create(method))) {
                final Method existingMethod = containingProgram.getClass(ancestor).getMethod(method.getName());
                err.reportWronglyOverriddenMethod(existingMethod, method);
                existingMethodFound = true;
            }
        }
        if (!existingMethodFound) {
            this.features.add(method);
        }
    }
}
