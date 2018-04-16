package net.alexweinert.coolc.representations.cool.ast;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/** Defines simple phylum Expression */
public abstract class Expression extends TreeNode {
    private IdSymbol type = null;

    protected Expression(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    public IdSymbol getType() {
        return this.type;
    }

    public void setType(IdSymbol type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        Expression other = (Expression) obj;
        if(other.getLineNumber() != this.getLineNumber()) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

}