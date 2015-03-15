package net.alexweinert.coolc.processors.java;

import java.io.IOException;
import java.io.Writer;

class ExceptionCatchingFileWriter {
    private Writer writer;

    public ExceptionCatchingFileWriter(Writer writer) {
        this.writer = writer;
    }

    public void write(String string) {
        try {
            this.writer.write(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
