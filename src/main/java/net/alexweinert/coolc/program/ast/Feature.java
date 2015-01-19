package net.alexweinert.coolc.program.ast;

/** Defines simple phylum Feature */
public abstract class Feature extends TreeNode {
    protected Feature(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public abstract void typecheck(Class_ enclosingClass, ClassTable classTable, FeatureTable featureTable);

}