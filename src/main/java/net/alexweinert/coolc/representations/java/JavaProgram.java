package net.alexweinert.coolc.representations.java;

import java.util.List;

public class JavaProgram {
    private final List<JavaClass> classes;

    public JavaProgram(List<JavaClass> classes) {
        this.classes = classes;
    }

    public List<JavaClass> getClasses() {
        return classes;
    }

}
