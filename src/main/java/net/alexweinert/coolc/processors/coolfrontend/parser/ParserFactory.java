package net.alexweinert.coolc.processors.coolfrontend.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;

import net.alexweinert.coolc.processors.coolfrontend.lexer.Lexer;

public class ParserFactory {
    public static Parser createParserForFile(String path) throws FileNotFoundException {
        final Lexer lexer = new Lexer(new FileReader(path));
        lexer.set_filename(path);
        final Parser parser = new Parser(lexer);
        return parser;
    }
}
