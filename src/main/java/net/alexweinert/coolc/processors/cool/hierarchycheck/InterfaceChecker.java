package net.alexweinert.coolc.processors.cool.hierarchycheck;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.representations.cool.program.parsed.Attribute;
import net.alexweinert.coolc.representations.cool.program.parsed.ClassNode;
import net.alexweinert.coolc.representations.cool.program.parsed.Classes;
import net.alexweinert.coolc.representations.cool.program.parsed.Feature;
import net.alexweinert.coolc.representations.cool.program.parsed.Features;
import net.alexweinert.coolc.representations.cool.program.parsed.Method;
import net.alexweinert.coolc.representations.cool.program.parsed.ParsedProgram;
import net.alexweinert.coolc.representations.cool.program.parsed.ParsedProgramVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

class InterfaceChecker extends ParsedProgramVisitor {
    private List<ClassNode> classes = new LinkedList<>();
    private Map<IdSymbol, List<Attribute>> attributes = new HashMap<>();
    private Map<IdSymbol, List<Method>> methods = new HashMap<>();
    private ParsedProgram containingProgram;

    private final SemanticErrorReporter error;

    InterfaceChecker(SemanticErrorReporter error) {
        this.error = error;
    }

    InterfaceChecker(List<ClassNode> classes, Map<IdSymbol, List<Attribute>> attributes,
            Map<IdSymbol, List<Method>> methods, ParsedProgram containingProgram, SemanticErrorReporter error) {
        this.classes = classes;
        this.attributes = attributes;
        this.methods = methods;
        this.containingProgram = containingProgram;
        this.error = error;
    }

    public static ParsedProgram checkInterfaces(ParsedProgram program, SemanticErrorReporter error) {
        final InterfaceChecker remover = new InterfaceChecker(error);
        program.acceptVisitor(remover);
        return remover.containingProgram;
    }

    @Override
    public void visitProgramPostorder(ParsedProgram program) {
        final Classes classes = new Classes(program.getFilename(), program.getLineNumber(), this.classes);
        this.containingProgram = new ParsedProgram(program.getFilename(), program.getLineNumber(), classes,
                program.getSymbolTables());
    }

    @Override
    public void visitClassPostorder(ClassNode classNode) {

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
        this.classes.add(new ClassNode(classNode.getFilename(), classNode.getLineNumber(), classNode.getIdentifier(),
                classNode.getParent(), features));
        this.attributes.clear();
        this.methods.clear();
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        if (!this.attributes.containsKey(attribute.getName())) {
            this.attributes.put(attribute.getName(), new LinkedList<Attribute>());
        }
        this.attributes.get(attribute.getName()).add(attribute);
    }

    @Override
    public void visitMethod(Method method) {
        if (!this.methods.containsKey(method.getName())) {
            this.methods.put(method.getName(), new LinkedList<Method>());
        }
        this.methods.get(method.getName()).add(method);
    }
}
