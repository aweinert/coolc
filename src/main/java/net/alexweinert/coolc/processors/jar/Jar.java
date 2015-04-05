package net.alexweinert.coolc.processors.jar;

import java.nio.file.Path;

public class Jar {
    final private Path relativePath;
    final private byte[] content;

    public Jar(Path relativePath, byte[] content) {
        this.relativePath = relativePath;
        this.content = content;
    }

    public Path getRelativePath() {
        return relativePath;
    }

    public byte[] getContent() {
        return content;
    }

}
