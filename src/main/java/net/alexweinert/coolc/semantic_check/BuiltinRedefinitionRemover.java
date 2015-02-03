package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.AbstractTable;

class BuiltinRedefinitionRemover {

    /**
     * Removes the redefinitions of builtin classes from the program, if they exist
     */
    public static Program removeBuiltinRedefinition(Program program, ISemanticErrorReporter out) {
        final Program withoutObjectClass = removeBuiltinClass(program, AbstractTable.stringtable.addString("Object"),
                out);
        final Program withoutBoolClass = removeBuiltinClass(program, AbstractTable.stringtable.addString("Bool"), out);
        final Program withoutIntClass = removeBuiltinClass(program, AbstractTable.stringtable.addString("Int"), out);
        final Program withoutStringClass = removeBuiltinClass(program, AbstractTable.stringtable.addString("String"),
                out);
        final Program withoutIOClass = removeBuiltinClass(program, AbstractTable.stringtable.addString("IO"), out);

        return withoutIOClass;
    }

    private static Program removeBuiltinClass(Program program, AbstractSymbol identifier, ISemanticErrorReporter out) {
        final Class classNode = program.getClass(identifier);
        if (classNode != null) {
            out.reportRedefinitionOfBuiltInClass(identifier, classNode);
            return new Program(program.getFilename(), program.getLineNumber(), program.getClasses().remove(classNode));
        }
        return program;
    }
}
