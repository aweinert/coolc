package net.alexweinert.coolc.representations.cool.expressions.typed;

import net.alexweinert.coolc.representations.cool.expressions.untyped.ExpressionVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * Defines AST constructor 'new_'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class TypedNew extends TypedExpression {
    final protected IdSymbol type_name;

    /**
     * Creates "new_" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for type_name
     */
    public TypedNew(String filename, int lineNumber, IdSymbol type, IdSymbol a1) {
        super(filename, lineNumber, type);
        type_name = a1;
    }

    @Override
    public void acceptVisitor(TypedExpressionVisitor visitor) {
        visitor.visitNew(this);
    }

    public IdSymbol getTypeIdentifier() {
        return type_name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((type_name == null) ? 0 : type_name.hashCode());
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
        TypedNew other = (TypedNew) obj;
        if (type_name == null) {
            if (other.type_name != null) {
                return false;
            }
        } else if (!type_name.equals(other.type_name)) {
            return false;
        }
        return true;
    }

}