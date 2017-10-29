package net.alexweinert.coolc.processors.cool.frontend.parser;

import java.io.Reader;

public interface ParserFactoryInterface {
    Parser createParserForReader(Reader reader);
}
