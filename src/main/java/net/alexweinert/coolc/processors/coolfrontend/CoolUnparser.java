package net.alexweinert.coolc.processors.coolfrontend;

import net.alexweinert.coolc.infrastructure.Backend;
import net.alexweinert.coolc.representations.cool.ast.Program;

public class CoolUnparser implements Backend<Program> {

    @Override
    public void process(Program input) {
        System.out.println(PrettyPrinter.printAst(input));
    }

}
