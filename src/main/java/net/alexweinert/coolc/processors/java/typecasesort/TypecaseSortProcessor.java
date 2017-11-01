package net.alexweinert.coolc.processors.java.typecasesort;

import net.alexweinert.pipelines.Processor;
import net.alexweinert.pipelines.ProcessorException;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.ast.Transformer;

public class TypecaseSortProcessor extends Processor<Program, Program> {

    @Override
    public Program process(Program input) throws ProcessorException {
        final Transformer transformer = new TypecaseSortTransformer(input.getHierarchy());
        return transformer.transform(input);
    }

}
