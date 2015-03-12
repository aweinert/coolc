package net.alexweinert.coolc.representations.cool.expressions.typed;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.util.TreeNode;

/** Defines simple phylum Expression */
public abstract class TypedExpression extends TreeNode {
    private final IdSymbol type;

    protected TypedExpression(String filename, int lineNumber, IdSymbol type) {
        super(filename, lineNumber);
        this.type = type;
    }

    public abstract void acceptVisitor(TypedExpressionVisitor visitor);
}