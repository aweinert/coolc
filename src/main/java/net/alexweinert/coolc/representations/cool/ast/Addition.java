package net.alexweinert.coolc.representations.cool.ast;


/**
 * Defines AST constructor 'plus'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Addition extends Expression {
    final private Expression e1;
    final private Expression e2;

    /**
     * Creates "plus" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a1
     *            initial value for e1
     * @param a2
     *            initial value for e2
     */
    public Addition(String filename, int lineNumber, Expression a1, Expression a2) {
        super(filename, lineNumber);
        this.e1 = a1;
        this.e2 = a2;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitAdditionPreorder(this);
        this.e1.acceptVisitor(visitor);
        visitor.visitAdditionInorder(this);
        this.e2.acceptVisitor(visitor);
        visitor.visitAdditionPostorder(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
        result = prime * result + ((e2 == null) ? 0 : e2.hashCode());
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
        Addition other = (Addition) obj;
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