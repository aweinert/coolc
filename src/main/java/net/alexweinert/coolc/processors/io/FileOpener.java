package net.alexweinert.coolc.processors.io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import net.alexweinert.coolc.infrastructure.Frontend;
import net.alexweinert.coolc.infrastructure.ProcessorException;

public class FileOpener extends Frontend<Reader> {

    private final String path;

    public FileOpener(String path) {
        this.path = path;
    }

    @Override
    public Reader process() throws ProcessorException {
        try {
            final FileReader reader = new FileReader(path);
            return reader;
        } catch (FileNotFoundException e) {
            throw new ProcessorException(e);
        }
    }

}
