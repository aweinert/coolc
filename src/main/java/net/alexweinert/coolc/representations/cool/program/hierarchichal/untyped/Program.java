package net.alexweinert.coolc.representations.cool.program.hierarchichal.untyped;

import net.alexweinert.coolc.representations.cool.program.hierarchichal.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.program.parsed.Classes;

/**
 * The complete program. Root node of a well-formed AST
 */
public class Program extends net.alexweinert.coolc.representations.cool.program.parsed.Program {
    private final ClassHierarchy hierarchy;

    public Program(String filename, int lineNumber, Classes classes, ClassHierarchy hierarchy) {
        super(filename, lineNumber, classes);
        this.hierarchy = hierarchy;
    }

    public ClassHierarchy getHierarchy() {
        return hierarchy;
    }

}