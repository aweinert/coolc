package net.alexweinert.coolc.processors.jbc;

import java.util.Collection;
import java.util.HashSet;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.io.File;
import net.alexweinert.coolc.representations.jbc.JbcClass;

public class JbcToFileProcessor extends Processor<Collection<JbcClass>, Collection<File>> {

    @Override
    public Collection<File> process(Collection<JbcClass> input) throws ProcessorException {
        final Collection<File> returnValue = new HashSet<>();
        for (JbcClass jbcClass : input) {
            returnValue.add(JbcEncoder.encode(jbcClass));
        }
        return returnValue;
    }
}
