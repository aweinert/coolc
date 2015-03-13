package net.alexweinert.coolc.representations.cool.ast;


/**
 * Defines AST constructor 'isvoid'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class IsVoid extends Expression {
    final protected Expression e1;

    /**
     * Creates "isvoid" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     */
    public IsVoid(String filename, int lineNumber, Expression a1) {
        super(filename, lineNumber);
        e1 = a1;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitIsVoidPreorder(this);
        this.e1.acceptVisitor(visitor);
        visitor.visitIsVoidPostorder(this);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
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
        IsVoid other = (IsVoid) obj;
        if (e1 == null) {
            if (other.e1 != null) {
                return false;
            }
        } else if (!e1.equals(other.e1)) {
            return false;
        }
        return true;
    }

}