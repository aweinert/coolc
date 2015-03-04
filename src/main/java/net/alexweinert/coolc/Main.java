package net.alexweinert.coolc;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.processors.coolfrontend.CoolFrontend;
import net.alexweinert.coolc.representations.cool.ast.Program;

public class Main {
    public static void main(String[] args) {
        final Processor<String, Program> toolchain = new CoolFrontend();
        toolchain.process(args[0]);
    }
}
