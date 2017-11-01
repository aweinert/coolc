package net.alexweinert.coolc.processors.cool.tohighlevel;

import net.alexweinert.pipelines.Processor;
import net.alexweinert.pipelines.ProcessorException;
import net.alexweinert.coolc.representations.cool.ast.Program;

public class CoolBackendProcessor<ClassType, ProgramType> extends Processor<Program, ProgramType> {

    final private CoolBackendBuilderFactory<ClassType, ProgramType> factory;

    public CoolBackendProcessor(CoolBackendBuilderFactory<ClassType, ProgramType> factory) {
        this.factory = factory;
    }

    @Override
    public ProgramType process(Program input) throws ProcessorException {
        final CoolBackendVisitor<ClassType, ProgramType> visitor = new CoolBackendVisitor<>(this.factory);
        return visitor.process(input);
    }

}
