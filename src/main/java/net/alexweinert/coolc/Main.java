package net.alexweinert.coolc;

import net.alexweinert.coolc.infrastructure.Compiler;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.processors.ProcessorBuilder;

import com.beust.jcommander.JCommander;

public class Main {
    public static void main(String[] args) {
        final Commandline commandline = Main.parseCommandline(args);
        if (commandline == null) {
            return;
        }

        final Compiler<?> compiler = Main.buildCompiler(commandline);
        try {
            compiler.compile();
        } catch (ProcessorException e) {
            e.printStackTrace();
        }
    }

    private static Commandline parseCommandline(String[] args) {
        final JCommander parser = new JCommander();
        parser.setProgramName("coolc");
        Commandline commandline = new Commandline(parser);
        parser.addObject(commandline);
        parser.parse(args);
        return commandline;
    }

    private static Compiler<?> buildCompiler(Commandline commandline) {
        if (commandline.showHelp() || !commandline.isValid()) {
            return new ProcessorBuilder().showHelp(commandline.getParser());
        }

        final ProcessorBuilder processorBuilder = new ProcessorBuilder();
        processorBuilder.openFile(commandline.inputFiles.get(0)).parseAndCheckCool();

        if (commandline.backend.toLowerCase().equals("java")) {
            final String output = commandline.output != null ? commandline.output : "output/";
            return processorBuilder.compileToJava().dumpJava(output);
        } else if (commandline.backend.toLowerCase().equals("jar")) {
            final String output = commandline.output != null ? commandline.output : "out.jar";
            return processorBuilder.compileToJava().compileJar(output);
        } else if (commandline.backend.toLowerCase().equals("jbc")) {
            final String output = commandline.output != null ? commandline.output : "output/";
            return processorBuilder.coolToBytecode().bytecodeToJbc().jbcToFile(output);
        } else {
            return new ProcessorBuilder().showHelp(commandline.getParser());
        }
    }
}
