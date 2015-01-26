package net.alexweinert.coolc.semantic_check;

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

public class DoubleFeatureRemover extends ASTVisitor {
    private List<Class> classes = new LinkedList<>();
    private List<Method> methods = new LinkedList<>();
    private List<Attribute> attributes = new LinkedList<>();
    private Program containingProgram;
    private Class containingClass;

    public static Program removeDoubleFeatures(Program program) {
        final DoubleFeatureRemover remover = new DoubleFeatureRemover();
        program.acceptVisitor(remover);
        return remover.containingProgram;
    }

    @Override
    public void visitProgramPostorder(Program program) {
        this.containingProgram = new Program(program.getLineNumber(),
                new Classes(program.getLineNumber(), this.classes));
    }

    @Override
    public void visitClassPreorder(Class classNode) {
        this.containingClass = classNode;
    }

    @Override
    public void visitClassPostorder(Class classNode) {
        final List<Feature> featuresList = new LinkedList<>();
        featuresList.addAll(this.attributes);
        featuresList.addAll(this.methods);
        final Features features = new Features(classNode.getLineNumber(), featuresList);
        this.classes.add(new Class(classNode.getLineNumber(), classNode.getIdentifier(), classNode.getParent(),
                features, classNode.getParent()));
        this.attributes.clear();
        this.methods.clear();
    }

    @Override
    public void visitAttributePostorder(Attribute attribute) {
        for (final Attribute existingAttribute : this.attributes) {
            if (existingAttribute.getName().equals(attribute.getName())) {
                System.out.println("ERROR: Multiple definition of attribute " + attribute.getName() + " in class "
                        + this.containingClass.getIdentifier());
                System.out.println("       Only respecting first definition, ignoring subsequent ones");
                return;
            }
        }
        this.attributes.add(attribute);
    }

    @Override
    public void visitMethodPostorder(Method method) {
        for (final Method existingMethod : this.methods) {
            if (existingMethod.getName().equals(method.getName())) {
                System.out.println("ERROR: Multiple definition of method " + method.getName() + " in class "
                        + this.containingClass.getIdentifier());
                System.out.println("       Only respecting first definition, ignoring subsequent ones");
                return;
            }
        }
        this.methods.add(method);
    }
}
