package net.alexweinert.coolc.processors.io;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

import net.alexweinert.coolc.infrastructure.Backend;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.io.File;

public class FileDumper implements Backend<Collection<File>> {

    private final Path outputPath;

    public FileDumper(Path outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public void process(Collection<File> input) throws ProcessorException {
        try {
            for (File file : input) {
                final Path path = this.outputPath.resolve(file.getPath());
                final OutputStream writer = new DataOutputStream(Files.newOutputStream(path, StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE));
                writer.write(file.getContent());
                writer.close();
            }
        } catch (IOException e) {
            throw new ProcessorException(e);
        }

    }
}
