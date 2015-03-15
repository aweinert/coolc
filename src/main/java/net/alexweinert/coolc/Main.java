package net.alexweinert.coolc;

import net.alexweinert.coolc.infrastructure.Compiler;
import net.alexweinert.coolc.processors.ProcessorBuilder;

public class Main {
    public static void main(String[] args) {
        final Compiler<String> compiler = new ProcessorBuilder().openFile(args[0]).parseAndCheckCool().removeSelfType()
                .dumpToConsole();
        compiler.compile();
    }
}
