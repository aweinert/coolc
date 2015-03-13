package net.alexweinert.coolc.processors.cool.frontend.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import net.alexweinert.coolc.processors.cool.frontend.lexer.Lexer;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;
import net.alexweinert.coolc.representations.cool.symboltables.IntTable;
import net.alexweinert.coolc.representations.cool.symboltables.StringTable;

public class ParserFactory {
    public static Parser createParserForFile(String path) throws FileNotFoundException {
        final Lexer lexer = new Lexer(new FileReader(path));
        lexer.set_filename(path);
        final Parser parser = new Parser(lexer);
        return parser;
    }

    public static Parser createParserFromReader(Reader reader) {
        final Lexer lexer = new Lexer(reader);
        lexer.setIdTable(IdTable.getInstance());
        lexer.setIntTable(IntTable.getInstance());
        lexer.setStringTable(StringTable.getInstance());
        final Parser parser = new Parser(lexer);
        return parser;
    }
}
