package net.alexweinert.coolc.processors.cool.hierarchycheck;

import java.util.Collection;
import java.util.LinkedList;

import net.alexweinert.coolc.representations.cool.program.parsed.ClassNode;
import net.alexweinert.coolc.representations.cool.program.parsed.Classes;
import net.alexweinert.coolc.representations.cool.program.parsed.Program;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

class MultipleClassesRemover {

    /**
     * Checks for multiple definitions of classes with the same identifier and removes all but one definition of these
     */
    public static Program removeMultipleClassDefinitions(Program program, SemanticErrorReporter error) {
        final Collection<IdSymbol> visitedClasses = new LinkedList<>();
        Classes returnClasses = program.getClasses();
        for (ClassNode classNode : program.getClasses()) {
            if (visitedClasses.contains(classNode.getIdentifier())) {
                error.reportClassRedefinition(program.getClass(classNode.getIdentifier()), classNode);
                returnClasses = returnClasses.remove(classNode);
            } else {
                visitedClasses.add(classNode.getIdentifier());
            }
        }
        return program.setClasses(returnClasses);
    }
}
