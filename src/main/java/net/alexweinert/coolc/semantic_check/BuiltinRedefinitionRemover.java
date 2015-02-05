package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

class BuiltinRedefinitionRemover {

    /**
     * Removes the redefinitions of builtin classes from the program, if they exist
     */
    public static Program removeBuiltinRedefinition(Program program, ISemanticErrorReporter out) {
        final Program withoutObjectClass = removeBuiltinClass(program, IdTable.getInstance().addString("Object"), out);
        final Program withoutBoolClass = removeBuiltinClass(program, IdTable.getInstance().addString("Bool"), out);
        final Program withoutIntClass = removeBuiltinClass(program, IdTable.getInstance().addString("Int"), out);
        final Program withoutStringClass = removeBuiltinClass(program, IdTable.getInstance().addString("String"), out);
        final Program withoutIOClass = removeBuiltinClass(program, IdTable.getInstance().addString("IO"), out);

        return withoutIOClass;
    }

    private static Program removeBuiltinClass(Program program, IdSymbol identifier, ISemanticErrorReporter out) {
        final Class classNode = program.getClass(identifier);
        if (classNode != null) {
            out.reportRedefinitionOfBuiltInClass(identifier, classNode);
            return new Program(program.getFilename(), program.getLineNumber(), program.getClasses().remove(classNode));
        }
        return program;
    }
}
