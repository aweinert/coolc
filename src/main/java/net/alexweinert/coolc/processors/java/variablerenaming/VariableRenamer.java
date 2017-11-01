package net.alexweinert.coolc.processors.java.variablerenaming;

import net.alexweinert.pipelines.Processor;
import net.alexweinert.coolc.representations.cool.ast.Program;

public class VariableRenamer extends Processor<Program, Program> {

    @Override
    public Program process(Program input) {
        return VariableRenamingVisitor.renameVariables(input);
    }

}
