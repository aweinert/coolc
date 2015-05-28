package net.alexweinert.coolc.processors.cool.hierarchycheck;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.cool.ast.Program;

public class CoolHierarchyChecker extends Processor<Program, Program> {

    @Override
    public Program process(Program input) throws ProcessorException {
        return SemanticChecker.checkSemantics(input);
    }

}
