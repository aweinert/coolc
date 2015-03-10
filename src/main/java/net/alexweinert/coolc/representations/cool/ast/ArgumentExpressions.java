package net.alexweinert.coolc.representations.cool.ast;

import java.util.Collection;
import java.util.Iterator;

public class ArgumentExpressions extends ListNode<Expression> {
    public ArgumentExpressions(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    public ArgumentExpressions(String filename, int lineNumber, Collection<Expression> elements) {
        super(filename, lineNumber, elements);
    }

    @Override
    public ArgumentExpressions add(Expression node) {
        final Collection<Expression> newElements = this.copyElements();
        newElements.add(node);
        return new ArgumentExpressions(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public ArgumentExpressions remove(Expression node) {
        final Collection<Expression> newElements = this.copyElements();
        newElements.remove(node);
        return new ArgumentExpressions(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitArgumentExpressionsPreorder(this);
        final Iterator<Expression> iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().acceptVisitor(visitor);
            if (iterator.hasNext()) {
                visitor.visitArgumentExpressionsInorder(this);
            }
        }
        visitor.visitArgumentExpressionsPostorder(this);
    }

}
