package net.alexweinert.coolc.processors.cool.frontend.lexer;

import org.springframework.stereotype.Service;

import java.io.Reader;

@Service
public class LexerFactory implements LexerFactoryInterface {
    @Override
    public Lexer createLexerFromReader(Reader reader) {
        return new Lexer(reader);
    }
}
