package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Classes;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

class ParentDefinednessChecker {

    /**
     * Checks for all classes that its parent class is defined in the given program. If a parent class is not defined,
     * it is set to Object. The given program may not contain any classes that inherit from base classes, except for
     * Object
     */
    public static Program checkParentDefinedness(Program program, ISemanticErrorReporter err) {
        Classes returnClasses = program.getClasses();
        final IdSymbol objectId = IdTable.getInstance().addString("Object");
        for (Class classNode : program.getClasses()) {
            // We need only check for inheritance from Object here, due to the precondition of the method
            final boolean inheritsFromObject = classNode.getParent().equals(objectId);
            final boolean parentDefined = program.getClass(classNode.getParent()) != null;
            if (!parentDefined && !inheritsFromObject) {
                err.reportUndefinedParentClass(classNode);
                returnClasses = returnClasses.replace(classNode, classNode.setParent(objectId));
            }
        }

        if (returnClasses == program.getClasses()) {
            return program;
        } else {
            return program.setClasses(returnClasses);
        }
    }
}
