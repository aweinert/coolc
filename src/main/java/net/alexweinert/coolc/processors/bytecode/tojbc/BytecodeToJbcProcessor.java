package net.alexweinert.coolc.processors.bytecode.tojbc;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;

import net.alexweinert.coolc.infrastructure.Processor;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.bytecode.ByteClass;
import net.alexweinert.coolc.representations.io.File;

public class BytecodeToJbcProcessor extends Processor<Collection<ByteClass>, Collection<File>> {

    @Override
    public Collection<File> process(Collection<ByteClass> input) throws ProcessorException {
        final Collection<File> returnValue = new HashSet<>();
        for (ByteClass byteClass : input) {
            final File.Builder builder = new File.Builder(Paths.get(byteClass.getId() + ".class"));
            // magic number
            builder.appendContent(new byte[] { (byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe });
            builder.appendContent(new byte[] { 0x00, 0x00 }); // minor version
            builder.appendContent(new byte[] { 0x00, 0x33 }); // major version
            builder.appendContent(new byte[] { 0x00, 0x05 }); // constants_table_size
            builder.appendContent(new byte[] { (byte) 0x07, 0x00, 0x02 }); // Class-tag

            builder.appendContent((byte) 0x01); // UTF8-tag
            builder.appendContent((byte) 0x00); // String length (high)
            builder.appendContent((byte) 0x03); // String length (low)
            for (byte character : "Foo".getBytes()) {
                builder.appendContent(character);
            }

            builder.appendContent((byte) 0x07); // Class-tag
            builder.appendContent((byte) 0x00);
            builder.appendContent((byte) 0x04);

            builder.appendContent((byte) 0x01); // UTF8-tag
            builder.appendContent((byte) 0x00); // String length (high)
            builder.appendContent((byte) 0x06); // String length (low)
            for (byte character : "Object".getBytes()) {
                builder.appendContent(character);
            }

            builder.appendContent(new byte[] { 0x00, 0x00 }); // access flags
            builder.appendContent(new byte[] { 0x00, 0x01 }); // this id
            builder.appendContent(new byte[] { 0x00, 0x03 }); // super id

            builder.appendContent(new byte[] { 0x00, 0x00 });

            builder.appendContent(new byte[] { 0x00, 0x00 });

            builder.appendContent(new byte[] { 0x00, 0x00 });

            builder.appendContent(new byte[] { 0x00, 0x00 });
            returnValue.add(builder.build());
        }

        return returnValue;
    }
}
