package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.program.ast.Classes;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.symboltables.IdTable;

class ParentDefinednessChecker {

    /**
     * Checks for all classes that its parent class is defined in the given program. If a parent class is not defined,
     * it is set to Object
     */
    public static Program checkParentDefinedness(Program program, ISemanticErrorReporter err) {
        Classes returnClasses = program.getClasses();
        for (Class classNode : program.getClasses()) {
            if (program.getClass(classNode.getParent()) == null) {
                err.reportUndefinedParentClass(classNode);
                returnClasses = returnClasses.remove(classNode).add(
                        classNode.setParent(IdTable.getInstance().addString("Object")));
            }
        }

        if (returnClasses == program.getClasses()) {
            return program;
        } else {
            return program.setClasses(returnClasses);
        }
    }
}
