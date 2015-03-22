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

        if (!commandline.isValid()) {
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
        if (commandline.showHelp()) {
            return new ProcessorBuilder().showHelp(commandline.getParser());
        } else {
            return new ProcessorBuilder().openFile(commandline.inputFiles.get(0)).parseAndCheckCool().compileToJava()
                    .compileJar("out.jar");
        }
    }
}
