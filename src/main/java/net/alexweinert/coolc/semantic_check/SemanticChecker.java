package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.program.ast.Program;

public class SemanticChecker {
    public static Program checkSemantics(Program program, Output out) {
        final SemanticErrorReporter error = new SemanticErrorReporter(out);

        program = MultipleClassesRemover.removeMultipleClassDefinitions(program, error);
        program = BuiltinRedefinitionRemover.removeBuiltinRedefinition(program, error);
        program = BuiltinInheritanceChecker.checkBuiltinInheritance(program, error);
        program = ParentDefinednessChecker.checkParentDefinedness(program, error);
        program = CircularInheritanceRemover.removeCircularInheritance(program, error);
        program = InterfaceChecker.checkInterfaces(program, error);
        program = OverridingChecker.checkOverriding(program, error);

        return program;
    }
}
