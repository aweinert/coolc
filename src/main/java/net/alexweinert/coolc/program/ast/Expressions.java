package net.alexweinert.coolc.program.ast;

import java.util.Collection;

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
    public Expressions addElement(Expression node) {
        final Collection<Expression> newElements = this.copyElements();
        newElements.add(node);
        return new Expressions(this.getLineNumber(), newElements);
    }
}