package net.alexweinert.coolc.representations.imperativeir;

import java.util.Collection;

public class Program {
    private final Collection<IRClass> classes;

    Program(Collection<IRClass> classes) {
        this.classes = classes;
    }
}
