package net.alexweinert.coolc;

import net.alexweinert.coolc.infrastructure.Compiler;
import net.alexweinert.coolc.processors.coolfrontend.CoolParser;
import net.alexweinert.coolc.processors.coolunparser.CoolUnparser;
import net.alexweinert.coolc.processors.fileopener.FileOpener;
import net.alexweinert.coolc.processors.stringdumper.StringDumper;

public class Main {
    public static void main(String[] args) {
        final Compiler<String> compiler = new FileOpener(args[0]).append(new CoolParser()).append(new CoolUnparser())
                .append(new StringDumper());
        compiler.compile();
    }
}
