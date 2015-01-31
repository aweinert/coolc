package net.alexweinert.coolc;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

import net.alexweinert.coolc.lexer.Lexer;
import net.alexweinert.coolc.parser.Parser;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.ast.visitors.PrettyPrinter;
import net.alexweinert.coolc.semantic_check.SemanticChecker;

public class Main {
    public static void main(String[] args) {
        final Program program;
        try {
            final Reader reader = new FileReader(args[0]);
            final Lexer lexer = new Lexer(reader);
            lexer.set_filename(args[0]);
            final Parser parser = new Parser(lexer);
            program = (Program) parser.parse().value;
            reader.close();
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
