package net.alexweinert.coolc.parser;

import java.awt.List;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;

import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Classes;
import net.alexweinert.coolc.program.ast.Features;
import net.alexweinert.coolc.program.ast.Program;
import net.alexweinert.coolc.program.symboltables.AbstractTable;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testClassParse() {
        try {
            final URI uri = this.getClass().getResource("/parser/positive/class-a.cl").toURI();
            final String path = Paths.get(uri).toString();
            final Parser parser = ParserFactory.createParserForFile(path);
            final Program actualProgram = (Program) parser.parse().value;
            final Program expectedProgram = new Program(path, 2, new Classes(path, 1,
                    Collections.singletonList(new Class(path, 1, AbstractTable.stringtable.addString("A"),
                            AbstractTable.stringtable.addString("Object"),
                            new Features(path, 1, Collections.EMPTY_LIST)))));
            Assert.assertEquals(expectedProgram, actualProgram);
        } catch (Throwable t) {
            Assert.fail(t.getMessage());
        }
    }

}
