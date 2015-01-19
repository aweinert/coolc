package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

/** Defines simple phylum Case */
public abstract class Case extends TreeNode {
    protected Case(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

}