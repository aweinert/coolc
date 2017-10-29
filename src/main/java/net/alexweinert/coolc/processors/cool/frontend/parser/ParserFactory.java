package net.alexweinert.coolc.processors.cool.frontend.parser;

import net.alexweinert.coolc.processors.cool.frontend.lexer.Lexer;
import net.alexweinert.coolc.processors.cool.frontend.lexer.LexerFactoryInterface;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;
import net.alexweinert.coolc.representations.cool.symboltables.IntTable;
import net.alexweinert.coolc.representations.cool.symboltables.StringTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Reader;

@Service
@Component
public class ParserFactory implements ParserFactoryInterface {
    private final LexerFactoryInterface lexerFactory;

    @Autowired
    public ParserFactory(LexerFactoryInterface lexerFactory) {
        this.lexerFactory = lexerFactory;
    }

    public Parser createParserForReader(Reader reader) {
        final Lexer lexer = this.lexerFactory.createLexerFromReader(reader);

        lexer.setIdTable(IdTable.getInstance());
        lexer.setIntTable(IntTable.getInstance());
        lexer.setStringTable(StringTable.getInstance());

        final Parser parser = new Parser(lexer);
        return parser;
    }
}
