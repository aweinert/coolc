package net.alexweinert.coolc.processors.java.jarcompile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.infrastructure.Backend;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.java.JavaClass;
import net.alexweinert.coolc.representations.java.JavaProgram;

public class JarCompiler implements Backend<JavaProgram> {
    private final Path pathToJar;

    public JarCompiler(String pathToJar) {
        this.pathToJar = Paths.get(pathToJar).toAbsolutePath();
    }

    @Override
    public void process(JavaProgram input) throws ProcessorException {
        try {
            final Path tempFolder = Files.createTempDirectory(null);
            final List<String> compilerArgs = new LinkedList<>();
            final List<String> jarArgs = new LinkedList<>();
            compilerArgs.add("javac");
            jarArgs.add("jar");
            jarArgs.add("cfe");
            jarArgs.add(pathToJar.toString());
            jarArgs.add("CoolMain");
            for (JavaClass javaClass : input.getClasses()) {
                final String pathToFile = tempFolder.resolve(javaClass.getIdentifier() + ".java").toString();
                final FileWriter fileWriter = new FileWriter(pathToFile);
                Writer writer = new BufferedWriter(fileWriter);
                writer.write(javaClass.getDefinition());
                writer.close();
                compilerArgs.add(pathToFile);
                final String pathToClass = javaClass.getIdentifier() + ".class";
                jarArgs.add(pathToClass);
            }
            final ProcessBuilder compilerBuilder = new ProcessBuilder(compilerArgs);
            final Process compiler = compilerBuilder.start();
            compiler.waitFor();
            if (compiler.exitValue() != 0) {
                throw new ProcessorException(new Exception("Error during compilation of generated Java files"));
            }

            final ProcessBuilder jarBuilder = new ProcessBuilder(jarArgs);
            jarBuilder.directory(tempFolder.toFile());
            final Process jar = jarBuilder.start();
            jar.waitFor();
            if (jar.exitValue() != 0) {
                throw new ProcessorException(new Exception("Error during packing of compiled Java classes"));
            }

            for (JavaClass javaClass : input.getClasses()) {
                Files.delete(tempFolder.resolve(javaClass.getIdentifier() + ".java"));
                Files.delete(tempFolder.resolve(javaClass.getIdentifier() + ".class"));
            }
            Files.delete(tempFolder);
        } catch (IOException | InterruptedException e) {
            throw new ProcessorException(e);
        }
    }
}
