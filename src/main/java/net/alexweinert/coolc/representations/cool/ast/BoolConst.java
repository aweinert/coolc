package net.alexweinert.coolc.representations.cool.ast;


/**
 * Defines AST constructor 'bool_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class BoolConst extends Expression {
    final protected Boolean val;

    /**
     * Creates "bool_const" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for val
     */
    public BoolConst(String filename, int lineNumber, Boolean a1) {
        super(filename, lineNumber);
        this.val = a1;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitBoolConst(this);
    }

    public Boolean getValue() {
        return this.val;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((val == null) ? 0 : val.hashCode());
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
        BoolConst other = (BoolConst) obj;
        if (val == null) {
            if (other.val != null) {
                return false;
            }
        } else if (!val.equals(other.val)) {
            return false;
        }
        return true;
    }

}