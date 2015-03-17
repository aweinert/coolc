package net.alexweinert.coolc.processors.java.fromcool;

import net.alexweinert.coolc.representations.java.JavaClass;

class JavaClassBuilder {
    private final String classId;
    private StringBuilder stringBuilder = new StringBuilder();

    public JavaClassBuilder(String classId) {
        this.classId = classId;
    }

    public void write(String string) {
        this.stringBuilder.append(string);
    }

    public JavaClass build() {
        return new JavaClass(this.classId, this.stringBuilder.toString());
    }

}
