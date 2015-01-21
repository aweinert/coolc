package net.alexweinert.coolc.program.ast;

import java.util.Collection;
import java.util.Vector;

/**
 * Defines list phylum Classes
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
public class Classes extends ListNode<Class> {

    protected Classes(int lineNumber, Collection<Class> elements) {
        super(lineNumber, elements);
    }

    /** Creates an empty "Classes" list */
    public Classes(int lineNumber) {
        super(lineNumber);
    }

    @Override
    public ListNode<Class> add(Class node) {
        final Collection<Class> newElements = this.copyElements();
        newElements.add(node);
        return new Classes(this.getLineNumber(), newElements);
    }
}