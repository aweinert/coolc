package net.alexweinert.coolc.semantic_check;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Classes;
import net.alexweinert.coolc.program.ast.Feature;
import net.alexweinert.coolc.program.ast.Features;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;

class DoubleFeatureRemover extends ASTVisitor {
    private List<Class> classes = new LinkedList<>();
    private Map<AbstractSymbol, List<Attribute>> attributes = new HashMap<>();
    private Map<AbstractSymbol, List<Method>> methods = new HashMap<>();
    private Program containingProgram;

    private final ISemanticErrorReporter error;

    DoubleFeatureRemover(ISemanticErrorReporter error) {
        this.error = error;
    }

    DoubleFeatureRemover(List<Class> classes, Map<AbstractSymbol, List<Attribute>> attributes,
            Map<AbstractSymbol, List<Method>> methods, Program containingProgram, ISemanticErrorReporter error) {
        this.classes = classes;
        this.attributes = attributes;
        this.methods = methods;
        this.containingProgram = containingProgram;
        this.error = error;
    }

    public static Program removeDoubleFeatures(Program program, ISemanticErrorReporter error) {
        final DoubleFeatureRemover remover = new DoubleFeatureRemover(error);
        program.acceptVisitor(remover);
        return remover.containingProgram;
    }

    @Override
    public void visitProgramPostorder(Program program) {
        final Classes classes = new Classes(program.getFilename(), program.getLineNumber(), this.classes);
        this.containingProgram = new Program(program.getFilename(), program.getLineNumber(), classes);
    }

    @Override
    public void visitClassPostorder(Class classNode) {

        final List<Feature> featuresList = new LinkedList<>();
        for (List<Attribute> attributes : this.attributes.values()) {
            if (attributes.size() > 1) {
                error.reportMultipleAttributes(classNode, attributes);
            }
            featuresList.add(attributes.get(0));
        }
        this.attributes.clear();
        for (List<Method> methods : this.methods.values()) {
            if (methods.size() > 1) {
                error.reportMultipleMethods(classNode, methods);
            }
            featuresList.add(methods.get(0));
        }
        this.methods.clear();

        final Features features = new Features(classNode.getFilename(), classNode.getLineNumber(), featuresList);
        this.classes.add(new Class(classNode.getFilename(), classNode.getLineNumber(), classNode.getIdentifier(),
                classNode.getParent(), features));
        this.attributes.clear();
        this.methods.clear();
    }

    @Override
    public void visitAttributePostorder(Attribute attribute) {
        if (!this.attributes.containsKey(attribute.getName())) {
            this.attributes.put(attribute.getName(), new LinkedList<Attribute>());
        }
        this.attributes.get(attribute.getName()).add(attribute);
    }

    @Override
    public void visitMethodPostorder(Method method) {
        if (!this.methods.containsKey(method.getName())) {
            this.methods.put(method.getName(), new LinkedList<Method>());
        }
        this.methods.get(method.getName()).add(method);
    }
}
