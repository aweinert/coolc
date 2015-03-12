package net.alexweinert.coolc.representations.cool.expressions.typed;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * Defines AST constructor 'no_expr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class TypedNoExpression extends TypedExpression {
    /**
     * Creates "no_expr" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     */
    public TypedNoExpression(String filename, int lineNumber, IdSymbol type) {
        super(filename, lineNumber, type);
    }

    @Override
    public void acceptVisitor(TypedExpressionVisitor visitor) {
        visitor.visitNoExpression(this);
    }

}