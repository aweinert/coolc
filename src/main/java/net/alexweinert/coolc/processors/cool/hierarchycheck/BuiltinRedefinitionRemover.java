package net.alexweinert.coolc.processors.cool.hierarchycheck;

import net.alexweinert.coolc.representations.cool.program.parsed.ClassNode;
import net.alexweinert.coolc.representations.cool.program.parsed.ParsedProgram;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

class BuiltinRedefinitionRemover {

    /**
     * Removes the redefinitions of builtin classes from the program, if they exist
     */
    public static ParsedProgram removeBuiltinRedefinition(ParsedProgram program, SemanticErrorReporter out) {
        final ParsedProgram withoutObjectClass = removeBuiltinClass(program, IdTable.getInstance().getObjectSymbol(),
                out);
        final ParsedProgram withoutBoolClass = removeBuiltinClass(withoutObjectClass, IdTable.getInstance()
                .getBoolSymbol(), out);
        final ParsedProgram withoutIntClass = removeBuiltinClass(withoutBoolClass,
                IdTable.getInstance().getIntSymbol(), out);
        final ParsedProgram withoutStringClass = removeBuiltinClass(withoutIntClass, IdTable.getInstance()
                .getStringSymbol(), out);
        final ParsedProgram withoutIOClass = removeBuiltinClass(withoutStringClass,
                IdTable.getInstance().getIOSymbol(), out);

        return withoutIOClass;
    }

    private static ParsedProgram removeBuiltinClass(ParsedProgram program, IdSymbol identifier,
            SemanticErrorReporter out) {
        final ClassNode classNode = program.getClass(identifier);
        if (classNode != null) {
            out.reportRedefinitionOfBuiltInClass(identifier, classNode);
            return program.removeClass(classNode);
        }
        return program;
    }
}
