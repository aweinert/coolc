package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.program.ast.Program;

public class SemanticChecker {
    public static Program checkSemantics(Program program, Output out) {

        program = BuiltinRemover.removeBuiltinClasses(program, out);
        program = BuiltinInheritanceChecker.checkBuiltinInheritance(program, out);
        program = ParentDefinednessChecker.checkParentDefinedness(program, out);
        program = CircularInheritanceRemover.removeCircularInheritance(program, out);
        program = InterfaceChecker.checkInterfaces(program, out);
        program = OverridingChecker.checkOverriding(program, out);

        return program;
    }
}
