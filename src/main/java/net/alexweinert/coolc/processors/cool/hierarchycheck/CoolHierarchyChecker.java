package net.alexweinert.coolc.processors.cool.hierarchycheck;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.representations.cool.program.hierarchichal.untyped.HierarchicalProgram;
import net.alexweinert.coolc.representations.cool.program.parsed.ParsedProgram;

public class CoolHierarchyChecker extends Processor<ParsedProgram, HierarchicalProgram> {

    @Override
    public HierarchicalProgram process(ParsedProgram input) {
        SemanticChecker.checkSemantics(input, new Output());
        return this.convertToHierarchical(input);
    }

    private HierarchicalProgram convertToHierarchical(ParsedProgram input) {
        return null;
    }

}
