package net.alexweinert.coolc.processors.cool.frontend.lexer;

import java.io.Reader;

public interface LexerFactoryInterface {
    Lexer createLexerFromReader(Reader reader);
}
