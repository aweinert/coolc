package net.alexweinert.coolc.representations.cool.expressions.typed;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * Defines AST constructor 'cond'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class TypedIf extends TypedExpression {
    final protected TypedExpression pred;
    final protected TypedExpression then_exp;
    final protected TypedExpression else_exp;

    /**
     * Creates "cond" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for pred
     * @param a1
     *            initial value for then_exp
     * @param a2
     *            initial value for else_exp
     */
    public TypedIf(String filename, int lineNumber, IdSymbol type, TypedExpression a1, TypedExpression a2,
            TypedExpression a3) {
        super(filename, lineNumber, type);
        pred = a1;
        then_exp = a2;
        else_exp = a3;
    }

    @Override
    public void acceptVisitor(TypedExpressionVisitor visitor) {
        visitor.visitIfPreorder(this);
        this.pred.acceptVisitor(visitor);
        visitor.visitIfPreorderOne(this);
        this.then_exp.acceptVisitor(visitor);
        visitor.visitIfPreorderTwo(this);
        this.else_exp.acceptVisitor(visitor);
        visitor.visitIfPostorder(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((else_exp == null) ? 0 : else_exp.hashCode());
        result = prime * result + ((pred == null) ? 0 : pred.hashCode());
        result = prime * result + ((then_exp == null) ? 0 : then_exp.hashCode());
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
        TypedIf other = (TypedIf) obj;
        if (else_exp == null) {
            if (other.else_exp != null) {
                return false;
            }
        } else if (!else_exp.equals(other.else_exp)) {
            return false;
        }
        if (pred == null) {
            if (other.pred != null) {
                return false;
            }
        } else if (!pred.equals(other.pred)) {
            return false;
        }
        if (then_exp == null) {
            if (other.then_exp != null) {
                return false;
            }
        } else if (!then_exp.equals(other.then_exp)) {
            return false;
        }
        return true;
    }

    public TypedExpression getCondition() {
        return this.pred;
    }

}