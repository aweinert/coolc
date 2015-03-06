package net.alexweinert.coolc;

import net.alexweinert.coolc.infrastructure.Compiler;
import net.alexweinert.coolc.processors.coolfrontend.CoolParser;
import net.alexweinert.coolc.processors.coolunparser.CoolUnparser;
import net.alexweinert.coolc.processors.fileopener.FileOpener;
import net.alexweinert.coolc.representations.cool.ast.Program;

public class Main {
    public static void main(String[] args) {
        final Compiler<Program> compiler = new FileOpener(args[0]).append(new CoolParser()).append(new CoolUnparser());
        compiler.compile();
    }
}
