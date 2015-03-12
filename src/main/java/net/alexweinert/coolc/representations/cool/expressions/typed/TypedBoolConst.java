package net.alexweinert.coolc.representations.cool.expressions.typed;

import net.alexweinert.coolc.representations.cool.expressions.untyped.ExpressionVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * Defines AST constructor 'bool_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class TypedBoolConst extends TypedExpression {
    final protected Boolean val;

    /**
     * Creates "bool_const" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for val
     */
    public TypedBoolConst(String filename, int lineNumber, IdSymbol type, Boolean a1) {
        super(filename, lineNumber, type);
        this.val = a1;
    }

    @Override
    public void acceptVisitor(TypedExpressionVisitor visitor) {
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
        TypedBoolConst other = (TypedBoolConst) obj;
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