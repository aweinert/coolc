package net.alexweinert.coolc.representations.cool.ast;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * Defines AST constructor 'dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class FunctionCall extends Expression {
    final protected Expression expr;
    final protected IdSymbol name;
    final protected ArgumentExpressions actual;

    /**
     * Creates "dispatch" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for expr
     * @param a1
     *            initial value for name
     * @param a2
     *            initial value for actual
     */
    public FunctionCall(String filename, int lineNumber, Expression a1, IdSymbol a2, ArgumentExpressions a3) {
        super(filename, lineNumber);
        expr = a1;
        name = a2;
        actual = a3;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitFunctionCallPreorder(this);
        this.expr.acceptVisitor(visitor);
        visitor.visitFunctionCallInorder(this);
        this.actual.acceptVisitor(visitor);
        visitor.visitFunctionCallPostorder(this);
    }

    public IdSymbol getFunctionIdentifier() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((actual == null) ? 0 : actual.hashCode());
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
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
        FunctionCall other = (FunctionCall) obj;
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
        return true;
    }

    public ArgumentExpressions getArguments() {
        return this.actual;
    }

}