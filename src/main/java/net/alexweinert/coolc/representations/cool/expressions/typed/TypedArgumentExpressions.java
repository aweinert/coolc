package net.alexweinert.coolc.representations.cool.expressions.typed;

import java.util.Collection;
import java.util.Iterator;

import net.alexweinert.coolc.representations.cool.expressions.untyped.ExpressionVisitor;
import net.alexweinert.coolc.representations.cool.util.ListNode;

public class TypedArgumentExpressions extends ListNode<TypedExpression> {
    public TypedArgumentExpressions(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    public TypedArgumentExpressions(String filename, int lineNumber, Collection<TypedExpression> elements) {
        super(filename, lineNumber, elements);
    }

    @Override
    public TypedArgumentExpressions add(TypedExpression node) {
        final Collection<TypedExpression> newElements = this.copyElements();
        newElements.add(node);
        return new TypedArgumentExpressions(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public TypedArgumentExpressions remove(TypedExpression node) {
        final Collection<TypedExpression> newElements = this.copyElements();
        newElements.remove(node);
        return new TypedArgumentExpressions(this.getFilename(), this.getLineNumber(), newElements);
    }

    public void acceptVisitor(TypedExpressionVisitor visitor) {
        visitor.visitArgumentExpressionsPreorder(this);
        final Iterator<TypedExpression> iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().acceptVisitor(visitor);
            if (iterator.hasNext()) {
                visitor.visitArgumentExpressionsInorder(this);
            }
        }
        visitor.visitArgumentExpressionsPostorder(this);
    }

}
