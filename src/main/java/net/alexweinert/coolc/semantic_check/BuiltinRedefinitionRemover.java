package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.symboltables.AbstractTable;
import net.alexweinert.coolc.program.symboltables.IdSymbol;

class BuiltinRedefinitionRemover {

    /**
     * Removes the redefinitions of builtin classes from the program, if they exist
     */
    public static Program removeBuiltinRedefinition(Program program, ISemanticErrorReporter out) {
        final Program withoutObjectClass = removeBuiltinClass(program, AbstractTable.idtable.addString("Object"), out);
        final Program withoutBoolClass = removeBuiltinClass(program, AbstractTable.idtable.addString("Bool"), out);
        final Program withoutIntClass = removeBuiltinClass(program, AbstractTable.idtable.addString("Int"), out);
        final Program withoutStringClass = removeBuiltinClass(program, AbstractTable.idtable.addString("String"), out);
        final Program withoutIOClass = removeBuiltinClass(program, AbstractTable.idtable.addString("IO"), out);

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
