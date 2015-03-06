package net.alexweinert.coolc;

import net.alexweinert.coolc.infrastructure.Compiler;
import net.alexweinert.coolc.processors.coolfrontend.CoolFrontend;
import net.alexweinert.coolc.processors.coolfrontend.CoolUnparser;
import net.alexweinert.coolc.representations.cool.ast.Program;

public class Main {
    public static void main(String[] args) {
        final Compiler<Program> compiler = new CoolFrontend(args[0]).append(new CoolUnparser());
        compiler.compile();
    }
}
