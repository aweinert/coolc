package net.alexweinert.coolc.processors.util;

import java.nio.file.Paths;

import net.alexweinert.pipelines.Frontend;
import net.alexweinert.pipelines.ProcessorException;
import net.alexweinert.coolc.representations.io.File;

import com.beust.jcommander.JCommander;

public class UsageFrontend extends Frontend<String> {

    final private JCommander jCommander;

    public UsageFrontend(JCommander jCommander) {
        this.jCommander = jCommander;
    }

    @Override
    public String process() throws ProcessorException {
        final StringBuilder builder = new StringBuilder();
        this.jCommander.usage(builder);
        return builder.toString();
    }

}
