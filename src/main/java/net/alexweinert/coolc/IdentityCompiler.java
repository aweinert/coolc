package net.alexweinert.coolc;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import net.alexweinert.coolc.lexer.Lexer;
import net.alexweinert.coolc.parser.Parser;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.ast.visitors.PrettyPrinter;

public class IdentityCompiler {
    public static void main(String[] args) {
        try {
            final Reader reader = new FileReader(args[0]);
            final Parser parser = new Parser(new Lexer(reader));
            Program program = (Program) parser.parse().value;
            reader.close();
            System.out.println(PrettyPrinter.printAst(program));
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
