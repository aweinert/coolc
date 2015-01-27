package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;

/** Defines simple phylum Feature */
public abstract class Feature extends TreeNode {
    final protected AbstractSymbol name;

    protected Feature(String filename, int lineNumber, AbstractSymbol name) {
        super(filename, lineNumber);
        this.name = name;
    }

    public AbstractSymbol getName() {
        return this.name;
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public abstract void typecheck(Class enclosingClass, ClassTable classTable, FeatureTable featureTable);

}