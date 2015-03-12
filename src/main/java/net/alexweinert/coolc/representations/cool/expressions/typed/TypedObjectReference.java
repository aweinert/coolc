package net.alexweinert.coolc.representations.cool.expressions.typed;

// -*- mode: java -*-
//
// file: cool-tree.m4
//
// This file defines the AST
//
// ////////////////////////////////////////////////////////

import net.alexweinert.coolc.representations.cool.expressions.untyped.ExpressionVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * Defines AST constructor 'object'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class TypedObjectReference extends TypedExpression {
    final protected IdSymbol name;

    /**
     * Creates "object" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     */
    public TypedObjectReference(String filename, int lineNumber, IdSymbol type, IdSymbol a1) {
        super(filename, lineNumber, type);
        name = a1;
    }

    @Override
    public void acceptVisitor(TypedExpressionVisitor visitor) {
        visitor.visitObjectReference(this);
    }

    public IdSymbol getVariableIdentifier() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        TypedObjectReference other = (TypedObjectReference) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
