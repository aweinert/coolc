package net.alexweinert.coolc.representations.cool.expressions.typed;

import net.alexweinert.coolc.representations.cool.expressions.untyped.ExpressionVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.TreeConstants;

/**
 * Defines AST constructor 'eq'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class TypedEquality extends TypedExpression {
    final protected TypedExpression e1;
    final protected TypedExpression e2;

    /**
     * Creates "eq" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     * @param a1
     *            initial value for e2
     */
    public TypedEquality(String filename, int lineNumber, IdSymbol type, TypedExpression a1, TypedExpression a2) {
        super(filename, lineNumber, type);
        e1 = a1;
        e2 = a2;
    }

    private boolean isBasicType(IdSymbol candidate) {
        return candidate.equals(TreeConstants.Int) || candidate.equals(TreeConstants.Bool)
                || candidate.equals(TreeConstants.Str);
    }

    @Override
    public void acceptVisitor(TypedExpressionVisitor visitor) {
        visitor.visitEqualityPreorder(this);
        this.e1.acceptVisitor(visitor);
        visitor.visitEqualityInorder(this);
        this.e2.acceptVisitor(visitor);
        visitor.visitEqualityPostorder(this);
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
        TypedEquality other = (TypedEquality) obj;
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