package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

class BuiltinRedefinitionRemover {

    /**
     * Removes the redefinitions of builtin classes from the program, if they exist
     */
    public static Program removeBuiltinRedefinition(Program program, SemanticErrorReporter out) {
        final Program withoutObjectClass = removeBuiltinClass(program, IdTable.getInstance().getObjectSymbol(), out);
        final Program withoutBoolClass = removeBuiltinClass(withoutObjectClass, IdTable.getInstance().getBoolSymbol(),
                out);
        final Program withoutIntClass = removeBuiltinClass(withoutBoolClass, IdTable.getInstance().getIntSymbol(), out);
        final Program withoutStringClass = removeBuiltinClass(withoutIntClass, IdTable.getInstance().getStringSymbol(),
                out);
        final Program withoutIOClass = removeBuiltinClass(withoutStringClass, IdTable.getInstance().getIOSymbol(), out);

        return withoutIOClass;
    }

    private static Program removeBuiltinClass(Program program, IdSymbol identifier, SemanticErrorReporter out) {
        final Class classNode = program.getClass(identifier);
        if (classNode != null) {
            out.reportRedefinitionOfBuiltInClass(identifier, classNode);
            return new Program(program.getFilename(), program.getLineNumber(), program.getClasses().remove(classNode));
        }
        return program;
    }
}
