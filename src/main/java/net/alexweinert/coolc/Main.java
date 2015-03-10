package net.alexweinert.coolc;

import net.alexweinert.coolc.infrastructure.Compiler;
import net.alexweinert.coolc.processors.cool.frontend.CoolParser;
import net.alexweinert.coolc.processors.cool.typechecking.CoolTypeChecker;
import net.alexweinert.coolc.processors.cool.unparser.CoolUnparser;
import net.alexweinert.coolc.processors.io.fileopener.FileOpener;
import net.alexweinert.coolc.processors.io.stringdumper.StringDumper;

public class Main {
    public static void main(String[] args) {
        final Compiler<String> compiler = new FileOpener(args[0]).append(new CoolParser())
                .append(new CoolTypeChecker(new Output())).append(new CoolUnparser()).append(new StringDumper());
        compiler.compile();
    }
}
