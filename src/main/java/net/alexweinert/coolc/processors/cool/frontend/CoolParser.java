package net.alexweinert.coolc.processors.cool.frontend;

import java.io.Reader;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.processors.cool.frontend.lexer.Lexer;
import net.alexweinert.coolc.processors.cool.frontend.parser.Parser;
import net.alexweinert.coolc.processors.cool.frontend.parser.ParserFactory;
import net.alexweinert.coolc.representations.cool.ast.Program;

public class CoolParser extends Processor<Reader, Program> {

    final String filename;

    public CoolParser(final String filename) {
        this.filename = filename;
    }

    @Override
    public Program process(final Reader reader) throws ProcessorException {
        try {
            final Parser parser = ParserFactory.createParserFromReader(reader);
            ((Lexer) parser.getScanner()).set_filename(this.filename);
            final Object parseResult = parser.parse().value;
            final ParserErrorHandler handler = parser.getErrorHandler();
            if (handler.hasErrors()) {
                for (String error : handler.getErrorMessages()) {
                    System.out.println(error);
                }
                throw new ProcessorException(null);
            }
            return (Program) parseResult;
        } catch (Exception e) {
            throw new ProcessorException(e);
        }
    }
}
