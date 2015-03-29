package net.alexweinert.coolc.processors.bytecode.tojbc;

import java.util.Collection;
import java.util.HashSet;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.bytecode.ByteClass;
import net.alexweinert.coolc.representations.jbc.ClassFile;

public class BytecodeToJbcProcessor extends Processor<Collection<ByteClass>, Collection<ClassFile>> {

    @Override
    public Collection<ClassFile> process(Collection<ByteClass> input) throws ProcessorException {
        final Collection<ClassFile> returnValue = new HashSet<>();
        for (ByteClass byteClass : input) {
            final ClassFile.Builder builder = new ClassFile.Builder(byteClass.getId());
            builder.setParent(byteClass.getParent());
            returnValue.add(builder.build());
        }

        return returnValue;
    }

}
