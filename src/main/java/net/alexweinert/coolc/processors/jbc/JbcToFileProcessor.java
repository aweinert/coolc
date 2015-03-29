package net.alexweinert.coolc.processors.jbc;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Collection;

import net.alexweinert.coolc.infrastructure.Backend;
import net.alexweinert.coolc.infrastructure.ProcessorException;
import net.alexweinert.coolc.representations.jbc.ClassFile;

public class JbcToFileProcessor implements Backend<Collection<ClassFile>> {

    private final Path folderPath;

    public JbcToFileProcessor(Path folderPath) {
        this.folderPath = folderPath;
    }

    @Override
    public void process(Collection<ClassFile> input) throws ProcessorException {
        try {
            for (ClassFile file : input) {
                final FileOutputStream writer = new FileOutputStream(this.folderPath.resolve(
                        file.getClassId() + ".class").toString());

                writer.write(new byte[] { (byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe });
                writer.write(file.getMinorVersion());
                writer.write(file.getMajorVersion());

                writer.write(file.getConstantsCount());
                writer.write(file.getConstants());

                writer.write(file.getAccessFlags());
                writer.write(this.splitChar(file.getThisClassIndex()));
                writer.write(this.splitChar(file.getSuperClassIndex()));

                writer.write(new byte[] { 0x00, 0x00 });
                writer.write(file.getInterfaces());

                writer.write(new byte[] { 0x00, 0x00 });
                writer.write(file.getFields());

                writer.write(new byte[] { 0x00, 0x00 });
                writer.write(file.getMethods());

                writer.write(new byte[] { 0x00, 0x00 });
                writer.write(file.getAttributes());

                writer.close();
            }
        } catch (IOException e) {
            throw new ProcessorException(e);
        }
    }

    private byte[] splitChar(final char value) {
        return new byte[] { (byte) ((value >>> 8) & 0xFF), (byte) (value & 0xFF) };
    }
}
