package net.alexweinert.coolc.processors.cool.hierarchycheck;

import java.util.HashMap;
import java.util.Map;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.representations.cool.program.hierarchichal.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.program.hierarchichal.DeclaredClassSignature;
import net.alexweinert.coolc.representations.cool.program.hierarchichal.DefinedClassSignature;
import net.alexweinert.coolc.representations.cool.program.parsed.ClassNode;
import net.alexweinert.coolc.representations.cool.program.parsed.ParsedProgram;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

class SemanticChecker {
    public static ParsedProgram checkSemantics(ParsedProgram program, Output out) {
        final SemanticErrorReporter error = new SemanticErrorReporter(out);

        program = MultipleClassesRemover.removeMultipleClassDefinitions(program, error);
        program = BuiltinRedefinitionRemover.removeBuiltinRedefinition(program, error);
        program = BuiltinInheritanceChecker.checkBuiltinInheritance(program, error);
        program = ParentDefinednessChecker.checkParentDefinedness(program, error);
        program = CircularInheritanceRemover.removeCircularInheritance(program, error);

        final ClassHierarchy hierarchy = ClassHierarchy.create(program);
        program = InterfaceChecker.checkInterfaces(program, error);

        final Map<IdSymbol, DeclaredClassSignature> declaredSignatures = createDeclaredSignatures(program);
        program = OverridingChecker.checkOverriding(program, hierarchy, declaredSignatures, error);

        return program;
    }

    private static Map<IdSymbol, DeclaredClassSignature> createDeclaredSignatures(ParsedProgram program) {
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

    private static Map<IdSymbol, DefinedClassSignature> createDefinedSignatures(ParsedProgram program,
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
