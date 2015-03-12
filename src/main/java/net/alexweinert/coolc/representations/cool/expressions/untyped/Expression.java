package net.alexweinert.coolc.representations.cool.expressions.untyped;

import net.alexweinert.coolc.representations.cool.util.TreeNode;

/** Defines simple phylum Expression */
public abstract class Expression extends TreeNode {
    protected Expression(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    public abstract void acceptVisitor(ExpressionVisitor visitor);
}