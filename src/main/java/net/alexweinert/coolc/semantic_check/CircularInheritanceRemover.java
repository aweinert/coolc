package net.alexweinert.coolc.semantic_check;

import java.util.HashSet;
import java.util.Set;

import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Classes;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

class CircularInheritanceRemover {

    /**
     * Identifies classes that are part of a circular inheritance and removes them from the program. The given program
     * may not redefine any built-in classes
     */
    public static Program removeCircularInheritance(Program program, SemanticErrorReporter err) {
        Classes returnClasses = program.getClasses();
        final IdSymbol objectId = IdTable.getInstance().addString("Object");
        for (Class classNode : program.getClasses()) {
            final Set<IdSymbol> ancestorIds = getAncestors(program, classNode.getIdentifier());
            if (!ancestorIds.contains(objectId)) {
                final IdSymbol tieBreakerId = findTieBreaker(program, ancestorIds);
                err.reportCircularInheritance(getClasses(program, ancestorIds), program.getClass(tieBreakerId));
                final Class tieBreaker = program.getClass(tieBreakerId);
                returnClasses = returnClasses.replace(tieBreaker, tieBreaker.setParent(objectId));
            }
        }

        return program.setClasses(returnClasses);
    }

    /**
     * Returns the ancestors of the class with the given id. Return value contains the id of the class itself.
     */
    private static Set<IdSymbol> getAncestors(Program program, IdSymbol classId) {
        final IdSymbol objectId = IdTable.getInstance().addString("Object");
        final Set<IdSymbol> returnValue = new HashSet<>();
        IdSymbol currentAncestor = classId;
        do {
            returnValue.add(currentAncestor);
            currentAncestor = program.getClass(currentAncestor).getParent();
        } while (!currentAncestor.equals(objectId) && !returnValue.contains(currentAncestor));
        if (currentAncestor.equals(objectId)) {
            returnValue.add(objectId);
        }
        return returnValue;
    }

    /**
     * Computes the class whose parent shall be set to Object in order to break circular inheritance.
     * 
     * @param circularInheritance
     *            The set of id's of classes that form an inheritance circle. Must not be empty!
     */
    private static IdSymbol findTieBreaker(Program program, Set<IdSymbol> circularInheritance) {
        final Set<Class> classes = getClasses(program, circularInheritance);
        Class tieBreaker = null;
        for (Class classNode : classes) {
            if (tieBreaker == null || classNode.getLineNumber() < tieBreaker.getLineNumber()) {
                tieBreaker = classNode;
            }
        }
        return tieBreaker.getIdentifier();
    }

    /**
     * Converts a set of id's of classes into a set of classes with the given id, using program.getClass(id) on each.
     * TODO: Replace by identifiers.map, when/if possible
     */
    private static Set<Class> getClasses(Program program, Set<IdSymbol> identifiers) {
        final Set<Class> returnValue = new HashSet<>();
        for (IdSymbol id : identifiers) {
            returnValue.add(program.getClass(id));
        }
        return returnValue;
    }
}
