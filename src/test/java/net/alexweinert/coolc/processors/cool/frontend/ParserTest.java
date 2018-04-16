package net.alexweinert.coolc.processors.cool.frontend;

import net.alexweinert.coolc.representations.cool.ast.*;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.StringReader;
import java.util.Arrays;

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

        private Attribute Attribute(final int lineno, String name, String type, Expression init) {
            final IdTable idTable = IdTable.getInstance();
            return new Attribute(
                    this.path,
                    lineno,
                    idTable.addString(name),
                    idTable.addString(type),
                    init
            );
        }

        private NoExpression NoExpression(final int lineno) {
            return new NoExpression(this.path, lineno);
        }
    }

    @Test
    public void testEmpty() throws Exception {
        final String path = "empty.cl";
        final String program = "";

        final ProgramBuilder b = new ProgramBuilder(path);
        final Program expectedProgram = b.Program(1);

        final ApplicationContext context = new AnnotationConfigApplicationContext(CoolParser.class);
        final CoolParser parser = context.getBean(CoolParser.class);

        parser.setFilename(path);
        final Program actualProgram = parser.process(new StringReader(program));
        Assert.assertEquals(expectedProgram, actualProgram);
    }


    @Test
    public void testClassParse() throws Exception {
        final String path = "class-a.cl";
        final String program = "class A { };\n";

        final ProgramBuilder b = new ProgramBuilder(path);
        final Program expectedProgram = b.Program(1,
                b.Class(1, "A","Object"));

        final ApplicationContext context = new AnnotationConfigApplicationContext(CoolParser.class);
        final CoolParser parser = context.getBean(CoolParser.class);

        parser.setFilename(path);
        final Program actualProgram = parser.process(new StringReader(program));
        Assert.assertEquals(expectedProgram, actualProgram);
    }

    @Test
    public void testTwoClassParse() throws Exception {
        final String path = "class-b.cl";
        final String program =
                "class A { };\n" +
                "\n" +
                "class B {};\n";

        final ProgramBuilder b = new ProgramBuilder(path);
        final Program expectedProgram = b.Program(3,
                b.Class(1, "A","Object"),
                b.Class(3, "B","Object"));

        final ApplicationContext context = new AnnotationConfigApplicationContext(CoolParser.class);
        final CoolParser parser = context.getBean(CoolParser.class);

        parser.setFilename(path);
        final Program actualProgram = parser.process(new StringReader(program));
        Assert.assertEquals(expectedProgram, actualProgram);
    }

    @Test
    public void testEmptyLineStart() throws Exception {
        final String path = "empty-line-start.cl";
        final String program =
                "\n" +
                "\n" +
                "class A {};\n";

        final ProgramBuilder b = new ProgramBuilder(path);
        final Program expectedProgram = b.Program(3,
                b.Class(3, "A","Object"));

        final ApplicationContext context = new AnnotationConfigApplicationContext(CoolParser.class);
        final CoolParser parser = context.getBean(CoolParser.class);

        parser.setFilename(path);
        final Program actualProgram = parser.process(new StringReader(program));
        Assert.assertEquals(expectedProgram, actualProgram);
    }

    @Test
    public void testInheritance() throws Exception {
        final String path = "inheritance.cl";
        final String program = "class B inherits A {};\n";

        final ProgramBuilder b = new ProgramBuilder(path);
        final Program expectedProgram = b.Program(1,
                b.Class(1, "B","A"));

        final ApplicationContext context = new AnnotationConfigApplicationContext(CoolParser.class);
        final CoolParser parser = context.getBean(CoolParser.class);

        parser.setFilename(path);
        final Program actualProgram = parser.process(new StringReader(program));
        Assert.assertEquals(expectedProgram, actualProgram);
    }


    @Test
    public void testAttribute() throws Exception {
        final String path = "attribute.cl";
        final String program = "class A {\n" +
                "attr : Int;\n" +
                "};\n";

        final ProgramBuilder b = new ProgramBuilder(path);
        final Program expectedProgram =
                b.Program(3,
                    b.Class(3, "A","Object",
                            b.Attribute(2, "attr", "Int", b.NoExpression(2))
                    )
                );

        final ApplicationContext context = new AnnotationConfigApplicationContext(CoolParser.class);
        final CoolParser parser = context.getBean(CoolParser.class);

        parser.setFilename(path);
        final Program actualProgram = parser.process(new StringReader(program));
        Assert.assertEquals(expectedProgram, actualProgram);
    }
}
