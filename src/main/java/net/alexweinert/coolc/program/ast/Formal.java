package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

/** Defines simple phylum Formal */
public abstract class Formal extends TreeNode {
    protected Formal(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

}