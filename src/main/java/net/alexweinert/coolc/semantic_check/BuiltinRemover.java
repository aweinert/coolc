package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.symboltables.AbstractTable;

class BuiltinRemover {

    /**
     * Removes the redefinitions of builtin classes from the program, if they exist
     */
    public static Program removeBuiltinClasses(Program program, Output out) {
        final Program withoutObjectClass = removeBuiltinClass(program, "Object", out);
        final Program withoutBoolClass = removeBuiltinClass(program, "Bool", out);
        final Program withoutIntClass = removeBuiltinClass(program, "Int", out);
        final Program withoutStringClass = removeBuiltinClass(program, "String", out);
        final Program withoutIOClass = removeBuiltinClass(program, "IO", out);

        return withoutIOClass;
    }

    private static Program removeBuiltinClass(Program program, String identifier, Output out) {
        final Class classNode = program.getClass(AbstractTable.stringtable.addString(identifier));
        if (classNode != null) {
            final String errorString = String.format("Redefinition of builtin class %s at %s:%d", identifier,
                    classNode.getFilename(), classNode.getLineNumber());
            out.error(errorString);
            return new Program(program.getFilename(), program.getLineNumber(), program.getClasses().remove(classNode));
        }
        return program;
    }
}
