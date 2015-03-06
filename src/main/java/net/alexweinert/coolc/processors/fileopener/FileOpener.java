package net.alexweinert.coolc.processors.fileopener;

import java.io.FileReader;
import java.io.Reader;

import net.alexweinert.coolc.infrastructure.Frontend;

public class FileOpener extends Frontend<Reader> {

    private final String path;

    public FileOpener(String path) {
        this.path = path;
    }

    @Override
    public Reader process() {
        try {
            final FileReader reader = new FileReader(path);
            return reader;
        } catch (Throwable t) {
            return null;
        }
    }

}
