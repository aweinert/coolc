package net.alexweinert.coolc.processors.cool.frontend;

import net.alexweinert.coolc.representations.cool.ast.*;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileReader;
import java.io.StringReader;
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

        private ClassNode Class(final int lineno, final String name, final String parent, final Feature... features) {
            final IdTable idTable = IdTable.getInstance();
            return new ClassNode(
                    this.path,
                    lineno,
                    idTable.addString(name),
                    idTable.addString(parent),
                    new Features(this.path, lineno, Arrays.asList(features))
            );
        }
    }

    @Test
    public void testClassParse() throws Exception {
        final String path = "class-a.cl";
        final String program = "class A { };";

        final ProgramBuilder b = new ProgramBuilder(path);
        final Program expectedProgram = b.Program(1,
                b.Class(1, "A","Object"));

        final ApplicationContext context = new AnnotationConfigApplicationContext(CoolParser.class);
        final CoolParser parser = context.getBean(CoolParser.class);

        parser.setFilename(path);
        final Program actualProgram = parser.process(new StringReader(program));
        Assert.assertEquals(expectedProgram, actualProgram);
    }
}
