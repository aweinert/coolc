package net.alexweinert.coolc.representations.cool.ast;

import java.util.Iterator;

import net.alexweinert.coolc.representations.cool.information.MethodSignature;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * Defines AST constructor 'method'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Method extends Feature {
    final protected Formals formals;
    final protected IdSymbol return_type;
    final protected Expression expr;

    /**
     * Creates "method" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for formals
     * @param a2
     *            initial value for return_type
     * @param a3
     *            initial value for expr
     */
    public Method(String filename, int lineNumber, IdSymbol a1, Formals a2, IdSymbol a3, Expression a4) {
        super(filename, lineNumber, a1);
        formals = a2;
        return_type = a3;
        expr = a4;
    }

    public Expression getBody() {
        return this.expr;
    }

    public Formals getFormals() {
        return this.formals;
    }

    public IdSymbol getReturnType() {
        return this.return_type;
    }

    public MethodSignature getSignature() {
        return (new MethodSignature.Factory()).create(this);
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitMethodPreorder(this);
        this.formals.acceptVisitor(visitor);
        visitor.visitMethodInorder(this);
        this.expr.acceptVisitor(visitor);
        visitor.visitMethodPostorder(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
        result = prime * result + ((formals == null) ? 0 : formals.hashCode());
        result = prime * result + ((return_type == null) ? 0 : return_type.hashCode());
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
        Method other = (Method) obj;
        if (expr == null) {
            if (other.expr != null) {
                return false;
            }
        } else if (!expr.equals(other.expr)) {
            return false;
        }
        if (formals == null) {
            if (other.formals != null) {
                return false;
            }
        } else if (!formals.equals(other.formals)) {
            return false;
        }
        if (return_type == null) {
            if (other.return_type != null) {
                return false;
            }
        } else if (!return_type.equals(other.return_type)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.getName());
        builder.append(" : ");
        builder.append(this.getReturnType());
        builder.append(" (");
        final Iterator<Formal> it = this.formals.iterator();
        while (it.hasNext()) {
            builder.append(it.next().toString());
            if (it.hasNext()) {
                builder.append(", ");
            }
        }

        builder.append(") {\n\t");
        builder.append(this.expr);
        builder.append("\n}");
        return builder.toString();
    }
}