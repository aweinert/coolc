package net.alexweinert.coolc.processors.cool.typecheck;

import java.util.HashMap;
import java.util.Map;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.information.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.information.DeclaredClassSignature;
import net.alexweinert.coolc.representations.cool.information.DefinedClassSignature;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

public class CoolTypeChecker extends Processor<Program, Program> {

    private final TypeErrorReporter err;

    public CoolTypeChecker(Output err) {
        this.err = new TypeErrorReporter();
    }

    @Override
    public Program process(Program input) throws ProcessorException {
        final ClassHierarchy hierarchy = input.getHierarchy();
        final Map<IdSymbol, DeclaredClassSignature> declaredSignatures = CoolTypeChecker
                .createDeclaredSignatures(input);
        final Map<IdSymbol, DefinedClassSignature> definedSignatures = CoolTypeChecker.createDefinedSignatures(input,
                hierarchy, declaredSignatures);

        CoolTypeChecker.typecheck(input, definedSignatures, this.err);

        if (this.err.hasErrors()) {
            for (String errorMessage : this.err.getErrors()) {
                System.out.println(errorMessage);
                throw new ProcessorException(null);
            }
        }

        return input;
    }

    private static void typecheck(Program program, Map<IdSymbol, DefinedClassSignature> definedSignatures,
            TypeErrorReporter err) {
        final ClassHierarchy hierarchy = program.getHierarchy();
        for (ClassNode classNode : program.getClasses()) {
            classNode.acceptVisitor(new ClassTypeChecker(classNode.getIdentifier(), definedSignatures, hierarchy, err));
        }
    }

    private static Map<IdSymbol, DeclaredClassSignature> createDeclaredSignatures(Program program) {
        final Map<IdSymbol, DeclaredClassSignature> declaredSignatures = new HashMap<>();
        declaredSignatures.put(IdTable.getInstance().getObjectSymbol(), DeclaredClassSignature.createObjectSignature());
        declaredSignatures.put(IdTable.getInstance().getIntSymbol(), DeclaredClassSignature.createIntSignature());
        declaredSignatures.put(IdTable.getInstance().getBoolSymbol(), DeclaredClassSignature.createBoolSignature());
        declaredSignatures.put(IdTable.getInstance().getStringSymbol(), DeclaredClassSignature.createStringSignature());
        declaredSignatures.put(IdTable.getInstance().getIOSymbol(), DeclaredClassSignature.createIOSignature());

        for (ClassNode classNode : program.getClasses()) {
            declaredSignatures.put(classNode.getIdentifier(), DeclaredClassSignature.create(classNode));
        }
        return declaredSignatures;
    }

    private static Map<IdSymbol, DefinedClassSignature> createDefinedSignatures(Program program,
            ClassHierarchy hierarchy, Map<IdSymbol, DeclaredClassSignature> declaredSignatures) {

        final Map<IdSymbol, DefinedClassSignature> definedSignatures = new HashMap<>();
        final IdSymbol objectSymbol = IdTable.getInstance().getObjectSymbol();
        definedSignatures.put(objectSymbol, DefinedClassSignature.create(objectSymbol, hierarchy, declaredSignatures));

        final IdSymbol boolSymbol = IdTable.getInstance().getBoolSymbol();
        definedSignatures.put(boolSymbol, DefinedClassSignature.create(boolSymbol, hierarchy, declaredSignatures));

        final IdSymbol intSymbol = IdTable.getInstance().getIntSymbol();
        definedSignatures.put(intSymbol, DefinedClassSignature.create(intSymbol, hierarchy, declaredSignatures));

        final IdSymbol stringSymbol = IdTable.getInstance().getStringSymbol();
        definedSignatures.put(stringSymbol, DefinedClassSignature.create(stringSymbol, hierarchy, declaredSignatures));

        final IdSymbol ioSymbol = IdTable.getInstance().getIOSymbol();
        definedSignatures.put(ioSymbol, DefinedClassSignature.create(ioSymbol, hierarchy, declaredSignatures));

        for (ClassNode classNode : program.getClasses()) {
            definedSignatures.put(classNode.getIdentifier(),
                    DefinedClassSignature.create(classNode.getIdentifier(), hierarchy, declaredSignatures));
        }
        return definedSignatures;
    }

}
