package net.alexweinert.coolc.processors.coolfrontend;

import java.nio.file.Path;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.processors.coolfrontend.parser.Parser;
import net.alexweinert.coolc.processors.coolfrontend.parser.ParserFactory;
import net.alexweinert.coolc.processors.coolfrontend.semantic_check.SemanticChecker;
import net.alexweinert.coolc.representations.cool.ast.Program;

public class CoolFrontend extends Processor<String, Program> {

    @Override
    public Program process(String input) {
        final Program program;
        try {
            final Parser parser = ParserFactory.createParserForFile(input);
            program = (Program) parser.parse().value;
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }

        SemanticChecker.checkSemantics(program, new Output());
        return program;
    }

}
