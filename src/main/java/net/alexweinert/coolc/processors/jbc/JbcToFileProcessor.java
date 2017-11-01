package net.alexweinert.coolc.processors.jbc;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;

import net.alexweinert.pipelines.Processor;
import net.alexweinert.pipelines.ProcessorException;
import net.alexweinert.coolc.representations.io.File;
import net.alexweinert.coolc.representations.jbc.JbcClass;

public class JbcToFileProcessor extends Processor<Collection<JbcClass>, Collection<File>> {

    @Override
    public Collection<File> process(Collection<JbcClass> input) throws ProcessorException {
        final Collection<File> returnValue = new HashSet<>();
        returnValue.add(this.loadStandardClass("CoolObject"));
        returnValue.add(this.loadStandardClass("CoolBool"));
        returnValue.add(this.loadStandardClass("CoolInt"));
        returnValue.add(this.loadStandardClass("CoolString"));
        returnValue.add(this.loadStandardClass("CoolIO"));
        for (JbcClass jbcClass : input) {
            returnValue.add(JbcEncoder.encode(jbcClass));
        }
        return returnValue;
    }

    private File loadStandardClass(String classId) {
        final String resourceName = classId + ".class";
        final Path targetPath = Paths.get(resourceName);

        try {
            final URI resourceURI = this.getClass().getClassLoader().getResource(resourceName).toURI();
            final Path pathToResource = Paths.get(resourceURI);
            return (new File.Builder(targetPath)).appendContent(Files.readAllBytes(pathToResource)).build();
        } catch (IOException | URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
