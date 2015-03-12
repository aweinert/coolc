package net.alexweinert.coolc.processors.cool.unparser;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.representations.cool.program.parsed.Program;

public class CoolUnparser extends Processor<Program, String> {

    @Override
    public String process(Program input) {
        return PrettyPrinter.printAst(input);
    }

}
