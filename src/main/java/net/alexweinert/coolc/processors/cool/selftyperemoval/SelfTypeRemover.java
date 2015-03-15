package net.alexweinert.coolc.processors.cool.selftyperemoval;

import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.representations.cool.ast.Attribute;
import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Classes;
import net.alexweinert.coolc.representations.cool.ast.Expression;
import net.alexweinert.coolc.representations.cool.ast.Feature;
import net.alexweinert.coolc.representations.cool.ast.Features;
import net.alexweinert.coolc.representations.cool.ast.Method;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.information.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

public class SelfTypeRemover extends Processor<Program, Program> {

    @Override
    public Program process(Program input) {
        final ClassHierarchy hierarchy = input.getHierarchy();

        final List<ClassNode> classes = new LinkedList<>();

        for (ClassNode classNode : input.getClasses()) {
            final List<Feature> features = new LinkedList<>();

            for (Attribute attribute : hierarchy.getDefinedAttributes(classNode.getIdentifier())) {
                final IdSymbol attributeType = removeSelfType(attribute.getDeclaredType(), classNode.getIdentifier());
                final Expression cleanedExpression = ExpressionSelfTypeRemover.removeSelfType(
                        classNode.getIdentifier(), attribute.getInitializer());

                final Attribute newAttribute = new Attribute(attribute.getFilename(), attribute.getLineNumber(),
                        attribute.getName(), attributeType, cleanedExpression);
                features.add(newAttribute);
            }

            for (Method method : hierarchy.getDefinedMethods(classNode.getIdentifier())) {
                final IdSymbol returnType = removeSelfType(method.getReturnType(), classNode.getIdentifier());
                final Expression cleanedExpression = ExpressionSelfTypeRemover.removeSelfType(
                        classNode.getIdentifier(), method.getBody());

                // Formals cannot include SELF_TYPE according to standard and common sense
                final Method newMethod = new Method(method.getFilename(), method.getLineNumber(), method.getName(),
                        method.getFormals(), returnType, cleanedExpression);
                features.add(newMethod);
            }

            final Features newFeatures = new Features(classNode.getFeatures().getFilename(), classNode.getFeatures()
                    .getLineNumber(), features);
            final ClassNode newClass = new ClassNode(classNode.getFilename(), classNode.getLineNumber(),
                    classNode.getIdentifier(), classNode.getParent(), newFeatures);
            classes.add(newClass);
        }

        final Classes newClasses = new Classes(input.getClasses().getFilename(), input.getClasses().getLineNumber(),
                classes);
        final Program newProgram = new Program(input.getFilename(), input.getLineNumber(), newClasses);
        return newProgram;
    }

    public static IdSymbol removeSelfType(IdSymbol maybeSelfType, IdSymbol containingClass) {
        if (maybeSelfType.equals(IdTable.getInstance().getSelfTypeSymbol())) {
            return containingClass;
        } else {
            return maybeSelfType;
        }

    }
}
