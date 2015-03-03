package net.alexweinert.coolc.processors.coolfrontend.semantic_check;

import java.util.Collection;
import java.util.LinkedList;

import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Classes;
import net.alexweinert.coolc.representations.cool.ast.Program;
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
