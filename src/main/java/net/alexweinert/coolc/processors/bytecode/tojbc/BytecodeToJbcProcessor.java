package net.alexweinert.coolc.processors.bytecode.tojbc;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.bytecode.ByteClass;
import net.alexweinert.coolc.representations.jbc.JbcClass;

public class BytecodeToJbcProcessor extends Processor<List<ByteClass>, Collection<JbcClass>> {

    @Override
    public Collection<JbcClass> process(List<ByteClass> input) throws ProcessorException {
        final Collection<JbcClass> returnValue = new HashSet<>();
        for (ByteClass byteClass : input) {
            final JbcClass.Builder builder = new JbcClass.Builder(byteClass.getId(), byteClass.getParent());
            returnValue.add(builder.build());
        }

        return returnValue;
    }
}
