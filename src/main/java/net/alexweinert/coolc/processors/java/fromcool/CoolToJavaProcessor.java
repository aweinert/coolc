package net.alexweinert.coolc.processors.java.fromcool;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.java.JavaProgram;

public class CoolToJavaProcessor extends Processor<Program, JavaProgram> {

    @Override
    public JavaProgram process(Program input) throws ProcessorException {
        final CoolToJavaVisitor visitor = new CoolToJavaVisitor();
        return visitor.process(input);
    }

}
