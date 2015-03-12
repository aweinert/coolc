package net.alexweinert.coolc.representations.cool.expressions.typed;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * Defines AST constructor 'loop'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class TypedLoop extends TypedExpression {
    final protected TypedExpression pred;
    final protected TypedExpression body;

    /**
     * Creates "loop" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for pred
     * @param a1
     *            initial value for body
     */
    public TypedLoop(String filename, int lineNumber, IdSymbol type, TypedExpression a1, TypedExpression a2) {
        super(filename, lineNumber, type);
        pred = a1;
        body = a2;
    }

    @Override
    public void acceptVisitor(TypedExpressionVisitor visitor) {
        visitor.visitLoopPreorder(this);
        this.pred.acceptVisitor(visitor);
        visitor.visitLoopInorder(this);
        this.body.acceptVisitor(visitor);
        visitor.visitLoopPostorder(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((body == null) ? 0 : body.hashCode());
        result = prime * result + ((pred == null) ? 0 : pred.hashCode());
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
        TypedLoop other = (TypedLoop) obj;
        if (body == null) {
            if (other.body != null) {
                return false;
            }
        } else if (!body.equals(other.body)) {
            return false;
        }
        if (pred == null) {
            if (other.pred != null) {
                return false;
            }
        } else if (!pred.equals(other.pred)) {
            return false;
        }
        return true;
    }

    public TypedExpression getCondition() {
        return this.pred;
    }

}