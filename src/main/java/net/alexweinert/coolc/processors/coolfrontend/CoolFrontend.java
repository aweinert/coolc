package net.alexweinert.coolc.processors.coolfrontend;

import net.alexweinert.coolc.Output;
import net.alexweinert.coolc.infrastructure.Frontend;
import net.alexweinert.coolc.processors.coolfrontend.parser.Parser;
import net.alexweinert.coolc.processors.coolfrontend.parser.ParserFactory;
import net.alexweinert.coolc.processors.coolfrontend.semantic_check.SemanticChecker;
import net.alexweinert.coolc.representations.cool.ast.Program;

public class CoolFrontend extends Frontend<Program> {

    private final String path;

    public CoolFrontend(String path) {
        this.path = path;
    }

    @Override
    public Program process() {
        final Program program;
        try {
            final Parser parser = ParserFactory.createParserForFile(this.path);
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
