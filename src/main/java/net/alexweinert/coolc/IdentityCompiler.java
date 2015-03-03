package net.alexweinert.coolc;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import net.alexweinert.coolc.processors.coolfrontend.lexer.Lexer;
import net.alexweinert.coolc.processors.coolfrontend.parser.Parser;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.ast.visitors.PrettyPrinter;

public class IdentityCompiler {
    public static void main(String[] args) {
        try {
            final Reader reader = new FileReader(args[0]);
            final Parser parser = new Parser(new Lexer(reader));
            Program program = (Program) parser.parse().value;
            reader.close();
            Writer writer = new FileWriter(args[1]);
            writer.write(PrettyPrinter.printAst(program));
            writer.close();
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
