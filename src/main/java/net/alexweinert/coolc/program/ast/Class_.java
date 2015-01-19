package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.symboltables.AbstractSymbol;

/** Defines simple phylum Class_ */
public abstract class Class_ extends TreeNode {
    protected Class_(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public abstract AbstractSymbol getName();

    public abstract AbstractSymbol getParent();

    public abstract AbstractSymbol getFilename();

    public abstract Features getFeatures();

}