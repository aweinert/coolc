package net.alexweinert.coolc.parser;

import java.net.URI;
import java.nio.file.Paths;
import java.util.Collections;

import net.alexweinert.coolc.processors.coolfrontend.parser.Parser;
import net.alexweinert.coolc.processors.coolfrontend.parser.ParserFactory;
import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Classes;
import net.alexweinert.coolc.representations.cool.ast.Features;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

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
            final Program expectedProgram = new Program(path, 1, new Classes(path, 1,
                    Collections.singletonList(new ClassNode(path, 1, IdTable.getInstance().addString("A"), IdTable
                            .getInstance().addString("Object"), new Features(path, 1, Collections.EMPTY_LIST)))));
            Assert.assertEquals(expectedProgram, actualProgram);
        } catch (Throwable t) {
            Assert.fail(t.getMessage());
        }
    }

}
