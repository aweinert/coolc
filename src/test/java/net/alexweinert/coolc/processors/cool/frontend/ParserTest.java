package net.alexweinert.coolc.processors.cool.frontend;

import net.alexweinert.coolc.representations.cool.ast.ClassNode;
import net.alexweinert.coolc.representations.cool.ast.Classes;
import net.alexweinert.coolc.representations.cool.ast.Features;
import net.alexweinert.coolc.representations.cool.ast.Program;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileReader;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Collections;

public class ParserTest {

    @Test
    public void testClassParse() throws Exception {
        final URI uri = this.getClass().getResource("/parser/positive/class-a.cl").toURI();
        final String path = Paths.get(uri).toString();

        // TODO: Actually *unit*-test parsers, i.e., leverage DI to mock some stuff
        final ApplicationContext context = new AnnotationConfigApplicationContext(CoolParser.class);
        final CoolParser parser = context.getBean(CoolParser.class);
        parser.setFilename("class-a.cl");

        final Program actualProgram = parser.process(new FileReader(path));
        final Program expectedProgram = new Program(path, 1, new Classes(path, 1,
                Collections.singletonList(new ClassNode(path, 1, IdTable.getInstance().addString("A"), IdTable
                        .getInstance().addString("Object"), new Features(path, 1, Collections.EMPTY_LIST)))));
        Assert.assertEquals(expectedProgram, actualProgram);
    }
}
