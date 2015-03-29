package net.alexweinert.coolc.representations.jbc;

public class ClassFile {
    public static class Builder {
        private final String id;
        private String parent = null;

        public Builder(String id) {
            this.id = id;
        }

        public Builder setParent(String parent) {
            this.parent = parent;
            return this;
        }

        public ClassFile build() {
            return new ClassFile();
        }
    }
}
