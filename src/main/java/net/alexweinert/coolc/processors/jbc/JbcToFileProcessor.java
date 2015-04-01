package net.alexweinert.coolc.processors.jbc;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Collection;

import net.alexweinert.coolc.infrastructure.Backend;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.io.File;

public class JbcToFileProcessor implements Backend<Collection<File>> {

    private final Path folderPath;

    public JbcToFileProcessor(Path folderPath) {
        this.folderPath = folderPath;
    }

    @Override
    public void process(Collection<File> input) throws ProcessorException {
        try {
            for (File file : input) {
                final FileOutputStream writer = new FileOutputStream(this.folderPath.resolve(file.getPath()).toString());
                writer.write(file.getContent());
                writer.close();
            }
        } catch (IOException e) {
            throw new ProcessorException(e);
        }
    }
}
