package net.alexweinert.coolc.representations.cool.ast;

// -*- mode: java -*-
//
// file: cool-tree.m4
//
// This file defines the AST
//
// ////////////////////////////////////////////////////////

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * Defines AST constructor 'object'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class ObjectReference extends Expression {
    final protected IdSymbol name;

    /**
     * Creates "object" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     */
    public ObjectReference(String filename, int lineNumber, IdSymbol a1) {
        super(filename, lineNumber);
        name = a1;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
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
        ObjectReference other = (ObjectReference) obj;
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
