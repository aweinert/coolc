package net.alexweinert.coolc.processors.cool.hierarchycheck;

import net.alexweinert.coolc.representations.cool.program.parsed.ClassNode;
import net.alexweinert.coolc.representations.cool.program.parsed.Classes;
import net.alexweinert.coolc.representations.cool.program.parsed.Program;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

class ParentDefinednessChecker {

    /**
     * Checks for all classes that its parent class is defined in the given program. If a parent class is not defined,
     * it is set to Object. The given program may not contain any classes that inherit from base classes, except for
     * Object
     */
    public static Program checkParentDefinedness(Program program, SemanticErrorReporter err) {
        Classes returnClasses = program.getClasses();
        final IdSymbol objectId = IdTable.getInstance().getObjectSymbol();
        for (ClassNode classNode : program.getClasses()) {
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
