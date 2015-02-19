package net.alexweinert.coolc.semantic_check;

import java.util.HashMap;
import java.util.Map;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.information.ClassHierarchy;
import net.alexweinert.coolc.program.information.DeclaredClassSignature;
import net.alexweinert.coolc.program.information.DefinedClassSignature;
import net.alexweinert.coolc.program.symboltables.IdSymbol;

public class SemanticChecker {
    public static Program checkSemantics(Program program, Output out) {
        final SemanticErrorReporter error = new SemanticErrorReporter(out);

        program = MultipleClassesRemover.removeMultipleClassDefinitions(program, error);
        program = BuiltinRedefinitionRemover.removeBuiltinRedefinition(program, error);
        program = BuiltinInheritanceChecker.checkBuiltinInheritance(program, error);
        program = ParentDefinednessChecker.checkParentDefinedness(program, error);
        program = CircularInheritanceRemover.removeCircularInheritance(program, error);

        final ClassHierarchy hierarchy = ClassHierarchy.create(program);
        program = InterfaceChecker.checkInterfaces(program, error);

        final Map<IdSymbol, DeclaredClassSignature> declaredSignatures = createDeclaredSignatures(program);
        program = OverridingChecker.checkOverriding(program, hierarchy, error);

        final Map<IdSymbol, DefinedClassSignature> definedSignatures = createDefinedSignatures(program, hierarchy,
                declaredSignatures);

        return program;
    }

    private static Map<IdSymbol, DeclaredClassSignature> createDeclaredSignatures(Program program) {
        final Map<IdSymbol, DeclaredClassSignature> declaredSignatures = new HashMap<>();
        for (Class classNode : program.getClasses()) {
            declaredSignatures.put(classNode.getIdentifier(), DeclaredClassSignature.create(classNode));
        }
        return declaredSignatures;
    }

    private static Map<IdSymbol, DefinedClassSignature> createDefinedSignatures(Program program,
            ClassHierarchy hierarchy, Map<IdSymbol, DeclaredClassSignature> declaredSignatures) {
        final Map<IdSymbol, DefinedClassSignature> definedSignatures = new HashMap<>();
        for (Class classNode : program.getClasses()) {
            definedSignatures.put(classNode.getIdentifier(),
                    DefinedClassSignature.create(classNode.getIdentifier(), hierarchy, declaredSignatures));
        }
        return definedSignatures;
    }
}
