package net.alexweinert.coolc.processors.cool.frontend;

import net.alexweinert.coolc.representations.cool.ast.*;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileReader;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

public class ParserTest {

    private class ProgramBuilder {
        private String path;

        private ProgramBuilder(final String path) {
            this.path = path;
        }

        private Program Program(final int lineno, final ClassNode... classes) {
            return new Program(this.path, lineno, new Classes(path, lineno, Arrays.asList(classes)));
        }

        private ClassNode Class(final int lineno, final String name, final String type, final Feature... features) {
            final IdTable idTable = IdTable.getInstance();
            return new ClassNode(
                    this.path,
                    lineno,
                    idTable.addString(name),
                    idTable.addString(type),
                    new Features(this.path, lineno, Arrays.asList(features))
            );
        }
    }

    @Test
    public void testClassParse() throws Exception {
        final URI uri = this.getClass().getResource("/parser/positive/class-a.cl").toURI();
        final String path = Paths.get(uri).toString();

        // TODO: Actually *unit*-test parsers, i.e., leverage DI to mock some stuff
        final ApplicationContext context = new AnnotationConfigApplicationContext(CoolParser.class);
        final CoolParser parser = context.getBean(CoolParser.class);
        parser.setFilename("class-a.cl");

        final ProgramBuilder b = new ProgramBuilder(path);

        final Program actualProgram = parser.process(new FileReader(path));
        final Program expectedProgram = b.Program(1,
                b.Class(1, "A","Object"));
        Assert.assertEquals(expectedProgram, actualProgram);
    }
}
