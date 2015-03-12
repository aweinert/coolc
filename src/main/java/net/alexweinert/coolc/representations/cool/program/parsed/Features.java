package net.alexweinert.coolc.representations.cool.program.parsed;

import java.util.Collection;
import java.util.Iterator;

import net.alexweinert.coolc.representations.cool.expressions.untyped.ExpressionVisitor;
import net.alexweinert.coolc.representations.cool.util.ListNode;

/**
 * Defines list phylum Features
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
public class Features extends ListNode<Feature> {
    public Features(String filename, int lineNumber, Collection<Feature> elements) {
        super(filename, lineNumber, elements);
    }

    /** Creates an empty "Features" list */
    public Features(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    @Override
    public Features add(Feature node) {
        final Collection<Feature> newElements = this.copyElements();
        newElements.add(node);
        return new Features(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public Features remove(Feature node) {
        final Collection<Feature> newElements = this.copyElements();
        newElements.remove(node);
        return new Features(this.getFilename(), this.getLineNumber(), newElements);
    }

    public void acceptVisitor(ParsedProgramVisitor visitor) {
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