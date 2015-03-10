package net.alexweinert.coolc.representations.cool.ast;


/**
 * Defines AST constructor 'block'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Block extends Expression {
    final protected BlockExpressions body;

    /**
     * Creates "block" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for body
     */
    public Block(String filename, int lineNumber, BlockExpressions a1) {
        super(filename, lineNumber);
        this.body = a1;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitBlockPreorder(this);
        this.body.acceptVisitor(visitor);
        visitor.visitBlockPostorder(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((body == null) ? 0 : body.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Block other = (Block) obj;
        if (body == null) {
            if (other.body != null) {
                return false;
            }
        } else if (!body.equals(other.body)) {
            return false;
        }
        return true;
    }

}