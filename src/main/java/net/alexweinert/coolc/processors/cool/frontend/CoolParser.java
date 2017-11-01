package net.alexweinert.coolc.processors.cool.frontend;

import java.io.Reader;

import java_cup.runtime.Symbol;
import net.alexweinert.pipelines.Processor;
import net.alexweinert.pipelines.ProcessorException;
import net.alexweinert.coolc.processors.cool.frontend.lexer.Lexer;
import net.alexweinert.coolc.processors.cool.frontend.parser.Parser;
import net.alexweinert.coolc.processors.cool.frontend.parser.ParserFactory;
import net.alexweinert.coolc.processors.cool.frontend.parser.ParserFactoryInterface;
import net.alexweinert.coolc.representations.cool.ast.Program;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan( { "net.alexweinert.coolc","net.alexweinert.coolc.processors.cool.frontend.parser","net.alexweinert.coolc.processors.cool.frontend.lexer" } )
public class CoolParser extends Processor<Reader, Program> {

    final ParserFactoryInterface parserFactory;
    final Logger logger;

    private String filename;

    @Autowired
    public CoolParser(final ParserFactoryInterface parserFactory, final Logger logger) {
        this.parserFactory = parserFactory;
        this.logger = logger;
    }

    public void setFilename(final String filename) { this.filename = filename; }

    @Override
    public Program process(final Reader reader) throws ProcessorException {
        final Parser parser = parserFactory.createParserForReader(reader);
        ((Lexer) parser.getScanner()).set_filename(this.filename);
        logger.trace("Created Parser");

        final Symbol parseResult;
        try {
            parseResult = parser.parse();
        } catch (Exception e) {
            logger.error("Internal error during parsing", e);
            throw new ProcessorException(e);
        }
        logger.trace("Finished parsing");

        final ParserErrorHandler handler = parser.getErrorHandler();
        if (handler.hasErrors()) {
            logger.trace("Errors during parsing");
            for (String error : handler.getErrorMessages()) {
                logger.error(error);
            }

            // TODO: Handle this gracefully, errors in user input are expected behavior
            final ProcessorException exception = new ProcessorException(null);
            throw exception;
        }

        logger.trace("No errors during parsing");
        return (Program) parseResult.value;
    }
}
