package net.alexweinert.coolc.representations.cool.ast;

import java.util.Collection;
import java.util.Iterator;

/**
 * Defines list phylum Formals
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
public class Formals extends ListNode<Formal> {
    public Formals(String filename, int lineNumber, Collection<Formal> elements) {
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

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitFormalsPreorder(this);
        final Iterator<Formal> iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().acceptVisitor(visitor);
            if (iterator.hasNext()) {
                visitor.visitFormalsInorder(this);
            }
        }
        visitor.visitFormalsPostorder(this);
    }
}