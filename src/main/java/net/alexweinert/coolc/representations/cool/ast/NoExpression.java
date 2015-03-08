package net.alexweinert.coolc.representations.cool.ast;

import net.alexweinert.coolc.representations.cool.ast.visitors.ASTVisitor;

/**
 * Defines AST constructor 'no_expr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class NoExpression extends Expression {
    /**
     * Creates "no_expr" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     */
    public NoExpression(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitNoExpression(this);
    }

}