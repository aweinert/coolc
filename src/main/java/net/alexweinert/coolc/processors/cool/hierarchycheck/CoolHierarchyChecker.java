package net.alexweinert.coolc.processors.cool.hierarchycheck;

import net.alexweinert.pipelines.Processor;
import net.alexweinert.pipelines.ProcessorException;
import net.alexweinert.coolc.representations.cool.ast.Program;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CoolHierarchyChecker extends Processor<Program, Program> {

    @Override
    public Program process(Program input) throws ProcessorException {
        ApplicationContext context = new AnnotationConfigApplicationContext(SemanticChecker.class);
        return context.getBean(SemanticChecker.class).checkSemantics(input);
    }

}
