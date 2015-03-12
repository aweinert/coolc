package net.alexweinert.coolc.representations.cool.program.parsed;

import java.util.Collection;
import java.util.Iterator;

import net.alexweinert.coolc.representations.cool.expressions.untyped.ExpressionVisitor;
import net.alexweinert.coolc.representations.cool.util.ListNode;

/**
 * Defines list phylum Formals
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
public class Formals extends ListNode<Formal> {
    protected Formals(String filename, int lineNumber, Collection<Formal> elements) {
        super(filename, lineNumber, elements);
    }

    /** Creates an empty "Formals" list */
    public Formals(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    @Override
    public Formals add(Formal node) {
        final Collection<Formal> newElements = this.copyElements();
        newElements.add(node);
        return new Formals(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public Formals remove(Formal node) {
        final Collection<Formal> newElements = this.copyElements();
        newElements.remove(node);
        return new Formals(this.getFilename(), this.getLineNumber(), newElements);
    }
}