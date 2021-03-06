package net.alexweinert.coolc.representations.cool.ast;

import java.util.Collection;
import java.util.Iterator;

/**
 * Defines list phylum Cases
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
public class Cases extends ListNode<Case> {
    public Cases(String filename, int lineNumber, Collection<Case> elements) {
        super(filename, lineNumber, elements);
    }

    /** Creates an empty "Cases" list */
    public Cases(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    @Override
    public Cases add(Case node) {
        final Collection<Case> newElements = this.copyElements();
        newElements.add(node);
        return new Cases(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public Cases remove(Case node) {
        final Collection<Case> newElements = this.copyElements();
        newElements.remove(node);
        return new Cases(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitCasesPreorder(this);
        final Iterator<Case> iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().acceptVisitor(visitor);
            if (iterator.hasNext()) {
                visitor.visitCasesInorder(this);
            }
        }
        visitor.visitCasesPostorder(this);
    }

}