package net.alexweinert.coolc.representations.cool.expressions.untyped;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * Defines AST constructor 'static_dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class StaticFunctionCall extends FunctionCall {
    final protected IdSymbol type_name;

    /**
     * Creates "static_dispatch" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a1
     *            initial value for expr
     * @param a2
     *            initial value for type_name
     * @param a3
     *            initial value for name
     * @param a4
     *            initial value for actual
     */
    public StaticFunctionCall(String filename, int lineNumber, Expression a1, IdSymbol a2, IdSymbol a3,
            ArgumentExpressions a4) {
        super(filename, lineNumber, a1, a3, a4);
        type_name = a2;
    }

    @Override
    public void acceptVisitor(ExpressionVisitor visitor) {
        visitor.visitStaticFunctionCallPreorder(this);
        this.expr.acceptVisitor(visitor);
        visitor.visitStaticFunctionCallInorder(this);
        this.actual.acceptVisitor(visitor);
        visitor.visitStaticFunctionCallPostorder(this);
    }

    public IdSymbol getStaticType() {
        return type_name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((actual == null) ? 0 : actual.hashCode());
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        StaticFunctionCall other = (StaticFunctionCall) obj;
        if (actual == null) {
            if (other.actual != null) {
                return false;
            }
        } else if (!actual.equals(other.actual)) {
            return false;
        }
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
        if (type_name == null) {
            if (other.type_name != null) {
                return false;
            }
        } else if (!type_name.equals(other.type_name)) {
            return false;
        }
        return true;
    }

    public Expression getCallee() {
        return this.expr;
    }

}