package net.alexweinert.coolc.processors.cool.frontend;

import java.io.Reader;

import java_cup.runtime.Symbol;
import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.processors.cool.frontend.lexer.Lexer;
import net.alexweinert.coolc.processors.cool.frontend.parser.Parser;
import net.alexweinert.coolc.processors.cool.frontend.parser.ParserFactory;
import net.alexweinert.coolc.processors.cool.frontend.parser.ParserFactoryInterface;
import net.alexweinert.coolc.representations.cool.ast.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan( { "net.alexweinert.coolc.processors.cool.frontend.parser","net.alexweinert.coolc.processors.cool.frontend.lexer" } )
public class CoolParser extends Processor<Reader, Program> {

    final ParserFactoryInterface parserFactory;

    private String filename;

    @Autowired
    public CoolParser(final ParserFactoryInterface parserFactory) {
        this.parserFactory = parserFactory;
    }

    public void setFilename(final String filename) { this.filename = filename; }

    @Override
    public Program process(final Reader reader) throws ProcessorException {
        try {
            final Parser parser = parserFactory.createParserForReader(reader);
            ((Lexer) parser.getScanner()).set_filename(this.filename);

            final Symbol parseResult = parser.parse();
            final ParserErrorHandler handler = parser.getErrorHandler();
            if (handler.hasErrors()) {
                for (String error : handler.getErrorMessages()) {
                    System.out.println(error);
                }
                throw new ProcessorException(null);
            }

            return (Program) parseResult.value;
        } catch (Exception e) {
            throw new ProcessorException(e);
        }
    }
}
