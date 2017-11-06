package net.alexweinert.coolc.processors.cool.hierarchycheck;

import java.util.Collection;
import java.util.LinkedList;

import com.sun.org.apache.xpath.internal.operations.Mult;
import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Classes;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan("net.alexweinert.coolc.processors.cool.hierarchycheck")
class MultipleClassesRemover {

    private final SemanticErrorReporter error;

    @Autowired
    public MultipleClassesRemover(final SemanticErrorReporter error) {
        this.error = error;
    }

    /**
     * Checks for multiple definitions of classes with the same identifier and removes all but one definition of these
     */
    public Program removeMultipleClassDefinitions(Program program) {
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
