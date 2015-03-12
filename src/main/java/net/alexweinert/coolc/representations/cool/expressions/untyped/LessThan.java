package net.alexweinert.coolc.representations.cool.expressions.untyped;



/**
 * Defines AST constructor 'lt'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class LessThan extends Expression {
    final protected Expression e1;
    final protected Expression e2;

    /**
     * Creates "lt" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     * @param a1
     *            initial value for e2
     */
    public LessThan(String filename, int lineNumber, Expression a1, Expression a2) {
        super(filename, lineNumber);
        e1 = a1;
        e2 = a2;
    }

    @Override
    public void acceptVisitor(ExpressionVisitor visitor) {
        visitor.visitLessThanPreorder(this);
        this.e1.acceptVisitor(visitor);
        visitor.visitLessThanInorder(this);
        this.e2.acceptVisitor(visitor);
        visitor.visitLessThanPostorder(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
        result = prime * result + ((e2 == null) ? 0 : e2.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        LessThan other = (LessThan) obj;
        if (e1 == null) {
            if (other.e1 != null) {
                return false;
            }
        } else if (!e1.equals(other.e1)) {
            return false;
        }
        if (e2 == null) {
            if (other.e2 != null) {
                return false;
            }
        } else if (!e2.equals(other.e2)) {
            return false;
        }
        return true;
    }

}