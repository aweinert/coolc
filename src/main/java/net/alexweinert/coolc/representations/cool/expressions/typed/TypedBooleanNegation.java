package net.alexweinert.coolc.representations.cool.expressions.typed;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * Defines AST constructor 'comp'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class TypedBooleanNegation extends TypedExpression {
    final protected TypedExpression e1;

    /**
     * Creates "comp" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     */
    public TypedBooleanNegation(String filename, int lineNumber, IdSymbol type, TypedExpression a1) {
        super(filename, lineNumber, type);
        this.e1 = a1;
    }

    @Override
    public void acceptVisitor(TypedExpressionVisitor visitor) {
        visitor.visitBooleanNegationPreorder(this);
        this.e1.acceptVisitor(visitor);
        visitor.visitBooleanNegationPostorder(this);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
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
        TypedBooleanNegation other = (TypedBooleanNegation) obj;
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