package net.alexweinert.coolc.processors.jbc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.io.File;

public class FileToJarProcessor extends Processor<Collection<File>, File> {

    private final String jarPath;
    private final String entryPoint;

    public FileToJarProcessor(String jarPath, String entryPoint) {
        this.jarPath = jarPath;
        this.entryPoint = entryPoint;
    }

    @Override
    public File process(Collection<File> input) throws ProcessorException {
        try {
            final Path tempFolder = Files.createTempDirectory(null);
            final ArrayList<String> arguments = new ArrayList<>();
            arguments.add("jar");
            arguments.add("cfe");
            arguments.add("output.jar");
            arguments.add(this.entryPoint);

            for (File file : input) {
                Files.write(tempFolder.resolve(file.getPath()), file.getContent());
                arguments.add(file.getPath().toString());
            }

            Runtime.getRuntime().exec(arguments.toArray(new String[arguments.size()]), null, tempFolder.toFile())
                    .waitFor();

            return (new File.Builder(Paths.get(this.jarPath))).appendContent(
                    Files.readAllBytes(tempFolder.resolve("output.jar"))).build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
