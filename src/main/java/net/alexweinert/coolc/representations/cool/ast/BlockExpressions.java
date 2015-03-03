package net.alexweinert.coolc.representations.cool.ast;

import java.util.Collection;
import java.util.Iterator;

import net.alexweinert.coolc.representations.cool.ast.visitors.ASTVisitor;

public class BlockExpressions extends ListNode<Expression> {

    public BlockExpressions(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    public BlockExpressions(String filename, int lineNumber, Collection<Expression> elements) {
        super(filename, lineNumber, elements);
    }

    @Override
    public BlockExpressions add(Expression node) {
        final Collection<Expression> newElements = this.copyElements();
        newElements.add(node);
        return new BlockExpressions(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public BlockExpressions remove(Expression node) {
        final Collection<Expression> newElements = this.copyElements();
        newElements.remove(node);
        return new BlockExpressions(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitBlockExpressionsPreorder(this);
        final Iterator<Expression> iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().acceptVisitor(visitor);
            if (iterator.hasNext()) {
                visitor.visitBlockExpressionsInorder(this);
            }
        }
        visitor.visitBlockExpressionsPostorder(this);
    }

}
