package net.alexweinert.coolc.processors.cool.hierarchycheck;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.representations.cool.program.parsed.Program;

public class CoolHierarchyChecker extends Processor<Program, Program> {

    @Override
    public Program process(Program input) {
        SemanticChecker.checkSemantics(input, new Output());
        return input;
    }

}
