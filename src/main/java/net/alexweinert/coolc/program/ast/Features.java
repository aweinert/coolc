package net.alexweinert.coolc.program.ast;

import java.util.Collection;

/**
 * Defines list phylum Features
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
public class Features extends ListNode<Feature> {
    protected Features(int lineNumber, Collection<Feature> elements) {
        super(lineNumber, elements);
    }

    /** Creates an empty "Features" list */
    public Features(int lineNumber) {
        super(lineNumber);
    }

    @Override
    public Features add(Feature node) {
        final Collection<Feature> newElements = this.copyElements();
        newElements.add(node);
        return new Features(this.getLineNumber(), newElements);
    }
}