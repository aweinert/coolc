package net.alexweinert.coolc.representations.cool.ast;


/**
 * Defines AST constructor 'typcase'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Typecase extends Expression {
    final protected Expression expr;
    final protected Cases cases;

    /**
     * Creates "typcase" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for expr
     * @param a1
     *            initial value for cases
     */
    public Typecase(String filename, int lineNumber, Expression a1, Cases a2) {
        super(filename, lineNumber);
        expr = a1;
        cases = a2;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitTypecasePreorder(this);
        this.expr.acceptVisitor(visitor);
        visitor.visitTypecaseInorder(this);
        this.cases.acceptVisitor(visitor);
        visitor.visitTypecasePostorder(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((cases == null) ? 0 : cases.hashCode());
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
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
        Typecase other = (Typecase) obj;
        if (cases == null) {
            if (other.cases != null) {
                return false;
            }
        } else if (!cases.equals(other.cases)) {
            return false;
        }
        if (expr == null) {
            if (other.expr != null) {
                return false;
            }
        } else if (!expr.equals(other.expr)) {
            return false;
        }
        return true;
    }

    public Cases getCases() {
        return this.cases;
    }

}