package net.alexweinert.coolc.representations.cool.expressions.typed;

import net.alexweinert.coolc.representations.cool.expressions.untyped.ExpressionVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * Defines AST constructor 'let'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class TypedLet extends TypedExpression {
    final protected IdSymbol identifier;
    final protected IdSymbol type_decl;
    final protected TypedExpression init;
    final protected TypedExpression body;

    /**
     * Creates "let" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for identifier
     * @param a1
     *            initial value for type_decl
     * @param a2
     *            initial value for init
     * @param a3
     *            initial value for body
     */
    public TypedLet(String filename, int lineNumber, IdSymbol type, IdSymbol a1, IdSymbol a2, TypedExpression a3,
            TypedExpression a4) {
        super(filename, lineNumber, type);
        identifier = a1;
        type_decl = a2;
        init = a3;
        body = a4;
    }

    @Override
    public void acceptVisitor(TypedExpressionVisitor visitor) {
        visitor.visitLetPreorder(this);
        this.init.acceptVisitor(visitor);
        visitor.visitLetInorder(this);
        this.body.acceptVisitor(visitor);
        visitor.visitLetPostorder(this);
    }

    public IdSymbol getVariableIdentifier() {
        return identifier;
    }

    public IdSymbol getDeclaredType() {
        return type_decl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((body == null) ? 0 : body.hashCode());
        result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
        result = prime * result + ((init == null) ? 0 : init.hashCode());
        result = prime * result + ((type_decl == null) ? 0 : type_decl.hashCode());
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
        TypedLet other = (TypedLet) obj;
        if (body == null) {
            if (other.body != null) {
                return false;
            }
        } else if (!body.equals(other.body)) {
            return false;
        }
        if (identifier == null) {
            if (other.identifier != null) {
                return false;
            }
        } else if (!identifier.equals(other.identifier)) {
            return false;
        }
        if (init == null) {
            if (other.init != null) {
                return false;
            }
        } else if (!init.equals(other.init)) {
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

    public TypedExpression getInitializer() {
        return this.init;
    }

}