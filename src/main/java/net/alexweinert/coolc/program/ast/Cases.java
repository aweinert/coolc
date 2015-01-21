package net.alexweinert.coolc.program.ast;

import java.util.Collection;

/**
 * Defines list phylum Cases
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
public class Cases extends ListNode<Case> {
    protected Cases(int lineNumber, Collection<Case> elements) {
        super(lineNumber, elements);
    }

    /** Creates an empty "Cases" list */
    public Cases(int lineNumber) {
        super(lineNumber);
    }

    @Override
    public Cases add(Case node) {
        final Collection<Case> newElements = this.copyElements();
        newElements.add(node);
        return new Cases(this.getLineNumber(), newElements);
    }

}