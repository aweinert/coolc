package net.alexweinert.coolc.representations.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class File {
    public static class Builder {
        private final Path path;
        private final List<Byte> content = new LinkedList<>();

        public Builder(Path path) {
            this.path = path;
        }

        public Builder appendContent(byte content) {
            this.content.add(content);
            return this;
        }

        public Builder appendContent(byte[] content) {
            for (byte contentByte : content) {
                this.content.add(contentByte);
            }
            return this;
        }

        public Builder appendContent(Collection<Byte> content) {
            this.content.addAll(content);
            return this;
        }

        public File build() {
            final byte[] contentArray = new byte[this.content.size()];
            for (int i = 0; i < this.content.size(); ++i) {
                contentArray[i] = (this.content.get(i));
            }
            return new File(this.path, contentArray);
        }
    }

    private final Path path;
    private final byte[] content;

    public File(Path path, byte[] content) {
        this.path = path;
        this.content = content;
    }

    public Path getPath() {
        return this.path;
    }

    public byte[] getContent() {
        return this.content;
    }

}
