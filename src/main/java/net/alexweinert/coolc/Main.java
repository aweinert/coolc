package net.alexweinert.coolc;

import net.alexweinert.coolc.infrastructure.Compiler;
import net.alexweinert.coolc.processors.ProcessorBuilder;
import net.alexweinert.coolc.representations.cool.ast.Program;

public class Main {
    public static void main(String[] args) {
        final Compiler<Program> compiler = new ProcessorBuilder().openFile(args[0]).parseAndCheckCool()
                .unparseToJava("output");
        compiler.compile();
    }
}
