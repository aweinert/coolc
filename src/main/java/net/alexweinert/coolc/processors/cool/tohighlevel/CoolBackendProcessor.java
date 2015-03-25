package net.alexweinert.coolc.processors.cool.tohighlevel;

import java.util.Collection;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.cool.ast.Program;

public class CoolBackendProcessor<T> extends Processor<Program, Collection<T>> {

    final private CoolBackendBuilderFactory<T> factory;

    public CoolBackendProcessor(CoolBackendBuilderFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public Collection<T> process(Program input) throws ProcessorException {
        final CoolBackendVisitor<T> visitor = new CoolBackendVisitor(this.factory);
        return visitor.process(input);
    }

}
