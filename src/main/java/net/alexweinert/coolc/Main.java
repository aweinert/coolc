package net.alexweinert.coolc;

import net.alexweinert.coolc.infrastructure.Compiler;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.processors.ProcessorBuilder;
import net.alexweinert.coolc.representations.java.JavaProgram;

public class Main {
    public static void main(String[] args) {
        final Compiler<JavaProgram> compiler = new ProcessorBuilder().openFile(args[0]).parseAndCheckCool()
                .compileToJava().dumpJava("output");
        try {
            compiler.compile();
        } catch (ProcessorException e) {
            e.printStackTrace();
        }
    }
}
