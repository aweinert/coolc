package net.alexweinert.coolc.processors.java.dump;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import net.alexweinert.pipelines.Backend;
import net.alexweinert.pipelines.ProcessorException;
import net.alexweinert.coolc.representations.java.JavaClass;
import net.alexweinert.coolc.representations.java.JavaProgram;

public class JavaDumper implements Backend<JavaProgram> {

    private final Path pathToFolder;

    public JavaDumper(Path pathToFolder) {
        this.pathToFolder = pathToFolder;
    }

    @Override
    public void process(JavaProgram input) throws ProcessorException {
        try {
            if (!pathToFolder.toFile().exists()) {
                Files.createDirectory(pathToFolder);
            } else if (!pathToFolder.toFile().isDirectory()) {
                throw new ProcessorException(new Exception(pathToFolder.toString()
                        + " already exists, but is no folder"));
            }
            for (JavaClass javaClass : input.getClasses()) {
                final String pathToFile = this.pathToFolder.resolve(javaClass.getIdentifier() + ".java").toString();
                final FileWriter fileWriter = new FileWriter(pathToFile);
                Writer writer = new BufferedWriter(fileWriter);
                writer.write(javaClass.getDefinition());
                writer.close();
            }
        } catch (IOException e) {
            throw new ProcessorException(e);
        }
    }
}
