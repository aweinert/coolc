package net.alexweinert.coolc.processors.util;

import net.alexweinert.coolc.infrastructure.Frontend;
import net.alexweinert.coolc.infrastructure.ProcessorException;

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
