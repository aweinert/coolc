package net.alexweinert.coolc.processors.cool.hierarchycheck;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.representations.cool.program.hierarchichal.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.program.hierarchichal.DeclaredClassSignature;
import net.alexweinert.coolc.representations.cool.program.hierarchichal.MethodSignature;
import net.alexweinert.coolc.representations.cool.program.parsed.Attribute;
import net.alexweinert.coolc.representations.cool.program.parsed.ClassNode;
import net.alexweinert.coolc.representations.cool.program.parsed.Feature;
import net.alexweinert.coolc.representations.cool.program.parsed.Method;
import net.alexweinert.coolc.representations.cool.program.parsed.ParsedProgram;
import net.alexweinert.coolc.representations.cool.program.parsed.ParsedProgramVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

class OverridingChecker extends ParsedProgramVisitor {
    /**
     * Checks that no class overrides its parent's attributes and that each class only overrides its parent's methods in
     * the allowed way (i.e., argument and return types match)
     */
    public static ParsedProgram checkOverriding(ParsedProgram program, ClassHierarchy hierarchy,
            Map<IdSymbol, DeclaredClassSignature> declaredSignatures, SemanticErrorReporter err) {
        final List<ClassNode> newClasses = new LinkedList<>();
        for (ClassNode classNode : program.getClasses()) {
            final OverridingChecker checker = new OverridingChecker(program, classNode, hierarchy, declaredSignatures,
                    err);
            classNode.acceptVisitor(checker);
            newClasses.add(classNode.setFeatures(checker.features));
        }

        return program.setClasses(newClasses);
    }

    final private ParsedProgram containingProgram;
    final private ClassNode containingClass;
    final private ClassHierarchy classHierarchy;
    final private Map<IdSymbol, DeclaredClassSignature> declaredSignatures;
    final private SemanticErrorReporter err;

    final private List<Feature> features = new LinkedList<>();

    private OverridingChecker(ParsedProgram containingProgram, ClassNode containingClass,
            ClassHierarchy classHierarchy, Map<IdSymbol, DeclaredClassSignature> declaredSignatures,
            SemanticErrorReporter err) {
        this.containingProgram = containingProgram;
        this.containingClass = containingClass;
        this.classHierarchy = classHierarchy;
        this.declaredSignatures = declaredSignatures;
        this.err = err;
    }

    @Override
    public void visitAttribute(Attribute attribute) {
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
    public void visitMethod(Method method) {
        boolean existingMethodFound = false;
        for (IdSymbol ancestor : this.classHierarchy.getStrictAncestors(this.containingClass.getIdentifier())) {
            if (ancestor.equals(IdTable.getInstance().getObjectSymbol())) {
                continue;
            }
            final MethodSignature parentSignature = declaredSignatures.get(ancestor).getMethodSignature(
                    method.getName());
            if (parentSignature != null && !parentSignature.equals(MethodSignature.getFactory().create(method))) {
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
