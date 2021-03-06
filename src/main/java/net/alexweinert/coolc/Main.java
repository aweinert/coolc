package net.alexweinert.coolc;

import net.alexweinert.pipelines.Compiler;
import net.alexweinert.pipelines.ProcessorException;
import net.alexweinert.coolc.processors.ProcessorBuilder;
import net.alexweinert.coolc.processors.ProcessorBuilder.CoolProgramCompilerBuilder;

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
            System.out.println("Error during compilation. Exiting.");
            System.out.println(e.getCause().toString());
            e.getCause().printStackTrace();
            System.exit(1);
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
            return new ProcessorBuilder.FrontendBuilder().helpToString(commandline.getParser()).stringToConsole();
        }

        final ProcessorBuilder.FrontendBuilder frontendBuilder = new ProcessorBuilder.FrontendBuilder();
        final CoolProgramCompilerBuilder compilerBuilder = frontendBuilder.openFile(commandline.inputFiles.get(0))
                .fileToCool(commandline.inputFiles.get(0)).checkCool();

        if (commandline.backend.toLowerCase().equals("java")) {
            final String output = commandline.output != null ? commandline.output : "output/";
            return compilerBuilder.coolToJava().javaToFiles().filesToHarddrive(output);
        } else if (commandline.backend.toLowerCase().equals("jar")) {
            final String output = commandline.output != null ? commandline.output : "out.jar";
            return compilerBuilder.coolToBytecode().bytecodeToJbc().jbcToFiles().filesToJar(output, "CoolMain")
                    .fileToHarddrive(".");
        } else if (commandline.backend.toLowerCase().equals("jbc")) {
            final String output = commandline.output != null ? commandline.output : "output/";
            return compilerBuilder.coolToBytecode().bytecodeToJbc().jbcToFiles().filesToHarddrive(output);
        } else {
            return new ProcessorBuilder.FrontendBuilder().helpToString(commandline.getParser()).stringToConsole();
        }
    }
}
