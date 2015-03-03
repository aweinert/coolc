package net.alexweinert.coolc;

import net.alexweinert.coolc.processors.coolfrontend.parser.Parser;
import net.alexweinert.coolc.processors.coolfrontend.parser.ParserFactory;
import net.alexweinert.coolc.processors.coolfrontend.semantic_check.SemanticChecker;
import net.alexweinert.coolc.representations.cool.ast.Program;

public class Main {
    public static void main(String[] args) {
        final Program program;
        try {
            final Parser parser = ParserFactory.createParserForFile(args[0]);
            program = (Program) parser.parse().value;
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
            // We need to explicitly return here since java does not recognize
            // System.exit as aborting control flow
            return;
        }

        SemanticChecker.checkSemantics(program, new Output());
    }
}
