package net.alexweinert.coolc.processors.jbc;

import java.nio.file.Path;
import java.util.Collection;

import net.alexweinert.coolc.infrastructure.Backend;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.jbc.ClassFile;

public class JbcToFileProcessor implements Backend<Collection<ClassFile>> {

    private final Path folderPath;

    public JbcToFileProcessor(Path folderPath) {
        this.folderPath = folderPath;
    }

    @Override
    public void process(Collection<ClassFile> input) throws ProcessorException {
        // TODO Auto-generated method stub

    }

}
