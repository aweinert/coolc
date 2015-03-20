package net.alexweinert.coolc.processors.java.typecasesort;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.ast.Transformer;

public class TypecaseSortProcessor extends Processor<Program, Program> {

    @Override
    public Program process(Program input) throws ProcessorException {
        final Transformer transformer = new TypecaseSortTransformer(input.getHierarchy());
        return transformer.transform(input);
    }

}
