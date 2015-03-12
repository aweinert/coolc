package net.alexweinert.coolc.processors.cool.typecheck;

import java.util.HashMap;
import java.util.Map;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.representations.cool.program.hierarchichal.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.program.hierarchichal.DeclaredClassSignature;
import net.alexweinert.coolc.representations.cool.program.hierarchichal.DefinedClassSignature;
import net.alexweinert.coolc.representations.cool.program.parsed.ClassNode;
import net.alexweinert.coolc.representations.cool.program.parsed.Program;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

public class CoolTypeChecker extends Processor<Program, Program> {

    private final TypeErrorReporter err;

    public CoolTypeChecker(Output err) {
        this.err = new TypeErrorReporter(err);
    }

    @Override
    public Program process(Program input) {
        final ClassHierarchy hierarchy = ClassHierarchy.create(input);
        final Map<IdSymbol, DeclaredClassSignature> declaredSignatures = CoolTypeChecker
                .createDeclaredSignatures(input);
        final Map<IdSymbol, DefinedClassSignature> definedSignatures = CoolTypeChecker.createDefinedSignatures(input,
                hierarchy, declaredSignatures);

        CoolTypeChecker.typecheck(input, hierarchy, definedSignatures, this.err);

        return input;
    }

    private static void typecheck(Program program, ClassHierarchy hierarchy,
            Map<IdSymbol, DefinedClassSignature> definedSignatures, TypeErrorReporter err) {
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
