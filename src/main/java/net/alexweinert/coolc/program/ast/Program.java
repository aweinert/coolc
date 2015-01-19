package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

/** Defines simple phylum Program */
public abstract class Program extends TreeNode {
    protected Program(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public abstract void semant();

}