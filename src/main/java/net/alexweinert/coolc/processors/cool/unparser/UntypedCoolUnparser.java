package net.alexweinert.coolc.processors.cool.unparser;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.representations.cool.program.parsed.Program;

public class UntypedCoolUnparser extends Processor<Program, String> {

    final private ProgramPrettyPrinter programPrinter;

    public UntypedCoolUnparser() {
        this.programPrinter = new ProgramPrettyPrinter();
    }

    @Override
    public String process(Program input) {
        return this.programPrinter.printProgram(input);
    }
}
