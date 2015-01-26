package net.alexweinert.coolc.program.ast;

import java.util.Collection;
import java.util.Iterator;

import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;

/**
 * Defines list phylum Features
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
public class Features extends ListNode<Feature> {
    public Features(int lineNumber, Collection<Feature> elements) {
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

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitFeaturesPreorder(this);
        final Iterator<Feature> iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().acceptVisitor(visitor);
            if (iterator.hasNext()) {
                visitor.visitFeaturesInorder(this);
            }
        }
        visitor.visitFeaturesPostorder(this);
    }
}