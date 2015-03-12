package net.alexweinert.coolc.processors.cool.frontend;

import java.io.Reader;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.processors.cool.frontend.parser.Parser;
import net.alexweinert.coolc.processors.cool.frontend.parser.ParserFactory;
import net.alexweinert.coolc.representations.cool.program.parsed.Program;

public class CoolParser extends Processor<Reader, Program> {

    @Override
    public Program process(final Reader reader) {
        final Program program;
        try {
            final Parser parser = ParserFactory.createParserFromReader(reader);
            program = (Program) parser.parse().value;
        } catch (Throwable e) {
            return null;
        }

        return program;
    }

}
