package net.alexweinert.coolc.processors.cool.hierarchycheck;

import net.alexweinert.pipelines.Processor;
import net.alexweinert.pipelines.ProcessorException;
import net.alexweinert.coolc.representations.cool.ast.Program;

public class CoolHierarchyChecker extends Processor<Program, Program> {

    @Override
    public Program process(Program input) throws ProcessorException {
        return SemanticChecker.checkSemantics(input);
    }

}
