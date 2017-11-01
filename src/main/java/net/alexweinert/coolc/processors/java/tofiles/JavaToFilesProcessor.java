package net.alexweinert.coolc.processors.java.tofiles;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;

import net.alexweinert.pipelines.Processor;
import net.alexweinert.pipelines.ProcessorException;
import net.alexweinert.coolc.representations.io.File;
import net.alexweinert.coolc.representations.java.JavaClass;
import net.alexweinert.coolc.representations.java.JavaProgram;

public class JavaToFilesProcessor extends Processor<JavaProgram, Collection<File>> {

    @Override
    public Collection<File> process(JavaProgram input) throws ProcessorException {
        final Collection<File> returnValue = new HashSet<>();
        for (JavaClass javaClass : input.getClasses()) {
            final Path path = Paths.get(javaClass.getIdentifier().toString() + ".java");
            final File.Builder builder = new File.Builder(path);
            builder.appendContent(javaClass.getDefinition().getBytes());
            returnValue.add(builder.build());
        }
        return returnValue;
    }

}
