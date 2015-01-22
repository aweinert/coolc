package net.alexweinert.coolc.program.ast;

import java.util.Collection;
import java.util.Iterator;

import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;

/**
 * Defines list phylum Expressions
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
public class Expressions extends ListNode<Expression> {
    protected Expressions(int lineNumber, Collection<Expression> elements) {
        super(lineNumber, elements);
    }

    /** Creates an empty "Expressions" list */
    public Expressions(int lineNumber) {
        super(lineNumber);
    }

    @Override
    public Expressions add(Expression node) {
        final Collection<Expression> newElements = this.copyElements();
        newElements.add(node);
        return new Expressions(this.getLineNumber(), newElements);
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitExpressionsPreorder(this);
        final Iterator<Expression> iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().acceptVisitor(visitor);
            if (iterator.hasNext()) {
                visitor.visitExpressionsInorder(this);
            }
        }
        visitor.visitExpressionsPostorder(this);
    }
}