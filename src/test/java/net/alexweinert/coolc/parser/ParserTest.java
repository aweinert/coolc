package net.alexweinert.coolc.parser;

import java.net.URI;
import java.nio.file.Paths;

import net.alexweinert.coolc.program.ast.Program;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testClassParse() {
        try {
            final URI uri = this.getClass().getResource("/parser/positive/class-a.cl").toURI();
            final String path = Paths.get(uri).toString();
            System.err.println(path);
            final Parser parser = ParserFactory.createParserForFile(path);
            final Program program = (Program) parser.parse().value;
            Assert.assertNotNull(program);
        } catch (Throwable t) {
            Assert.fail(t.getMessage());
        }
    }

}
