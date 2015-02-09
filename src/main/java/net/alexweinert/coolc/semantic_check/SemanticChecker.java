package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.program.ast.Program;

public class SemanticChecker {
    public static Program checkSemantics(Program program, Output out) {
        final SemanticErrorReporter error = new SemanticErrorReporter(out);

        program = BuiltinRedefinitionRemover.removeBuiltinRedefinition(program, error);
        program = BuiltinInheritanceChecker.checkBuiltinInheritance(program, error);
        program = ParentDefinednessChecker.checkParentDefinedness(program, error);
        program = CircularInheritanceRemover.removeCircularInheritance(program, out);
        program = InterfaceChecker.checkInterfaces(program, out);
        program = OverridingChecker.checkOverriding(program, out);

        return program;
    }
}
