package net.alexweinert.coolc.representations.cool.expressions.typed;

import java.util.Collection;
import java.util.Iterator;

import net.alexweinert.coolc.representations.cool.expressions.untyped.ExpressionVisitor;
import net.alexweinert.coolc.representations.cool.util.ListNode;

public class TypedBlockExpressions extends ListNode<TypedExpression> {

    public TypedBlockExpressions(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    public TypedBlockExpressions(String filename, int lineNumber, Collection<TypedExpression> elements) {
        super(filename, lineNumber, elements);
    }

    @Override
    public TypedBlockExpressions add(TypedExpression node) {
        final Collection<TypedExpression> newElements = this.copyElements();
        newElements.add(node);
        return new TypedBlockExpressions(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public TypedBlockExpressions remove(TypedExpression node) {
        final Collection<TypedExpression> newElements = this.copyElements();
        newElements.remove(node);
        return new TypedBlockExpressions(this.getFilename(), this.getLineNumber(), newElements);
    }

    public void acceptVisitor(TypedExpressionVisitor visitor) {
        visitor.visitBlockExpressionsPreorder(this);
        final Iterator<TypedExpression> iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().acceptVisitor(visitor);
            if (iterator.hasNext()) {
                visitor.visitBlockExpressionsInorder(this);
            }
        }
        visitor.visitBlockExpressionsPostorder(this);
    }

}
