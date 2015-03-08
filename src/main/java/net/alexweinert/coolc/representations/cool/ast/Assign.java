package net.alexweinert.coolc.representations.cool.ast;

import net.alexweinert.coolc.representations.cool.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * Defines AST constructor 'assign'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Assign extends Expression {
    final protected IdSymbol name;

    final protected Expression expr;

    /**
     * Creates "assign" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for expr
     */
    public Assign(String filename, int lineNumber, IdSymbol a1, Expression a2) {
        super(filename, lineNumber);
        this.name = a1;
        this.expr = a2;
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitAssignPreorder(this);
        this.expr.acceptVisitor(visitor);
        visitor.visitAssignPostorder(this);
    }

    public IdSymbol getVariableIdentifier() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Assign other = (Assign) obj;
        if (expr == null) {
            if (other.expr != null) {
                return false;
            }
        } else if (!expr.equals(other.expr)) {
            return false;
        }
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