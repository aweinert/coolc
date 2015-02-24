package net.alexweinert.coolc.program.ast;

import java.util.Collection;
import java.util.Iterator;

import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;

/**
 * Defines list phylum Expressions
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
public abstract class Expressions extends ListNode<Expression> {
    protected Expressions(String filename, int lineNumber, Collection<Expression> elements) {
        super(filename, lineNumber, elements);
    }

    /** Creates an empty "Expressions" list */
    public Expressions(String filename, int lineNumber) {
        super(filename, lineNumber);
    }
}