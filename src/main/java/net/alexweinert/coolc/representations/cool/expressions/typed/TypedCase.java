package net.alexweinert.coolc.representations.cool.expressions.typed;

import net.alexweinert.coolc.representations.cool.expressions.untyped.ExpressionVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.util.TreeNode;

/**
 * Defines AST constructor 'branch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class TypedCase extends TreeNode {
    final protected IdSymbol name;
    final protected IdSymbol type_decl;
    final protected TypedExpression expr;

    /**
     * Creates "branch" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for type_decl
     * @param a2
     *            initial value for expr
     */
    public TypedCase(String filename, int lineNumber, IdSymbol a1, IdSymbol a2, TypedExpression a3) {
        super(filename, lineNumber);
        this.name = a1;
        this.type_decl = a2;
        this.expr = a3;
    }

    public void acceptVisitor(TypedExpressionVisitor visitor) {
        visitor.visitCasePreorder(this);
        this.expr.acceptVisitor(visitor);
        visitor.visitCasePostorder(this);
    }

    public IdSymbol getVariableIdentifier() {
        return name;
    }

    public IdSymbol getDeclaredType() {
        return type_decl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type_decl == null) ? 0 : type_decl.hashCode());
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
        TypedCase other = (TypedCase) obj;
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
        if (type_decl == null) {
            if (other.type_decl != null) {
                return false;
            }
        } else if (!type_decl.equals(other.type_decl)) {
            return false;
        }
        return true;
    }

}