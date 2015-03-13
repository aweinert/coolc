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
public class ParsedFeatures extends ListNode<ParsedFeature> {
    public ParsedFeatures(String filename, int lineNumber, Collection<ParsedFeature> elements) {
        super(filename, lineNumber, elements);
    }

    /** Creates an empty "Features" list */
    public ParsedFeatures(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    @Override
    public ParsedFeatures add(ParsedFeature node) {
        final Collection<ParsedFeature> newElements = this.copyElements();
        newElements.add(node);
        return new ParsedFeatures(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public ParsedFeatures remove(ParsedFeature node) {
        final Collection<ParsedFeature> newElements = this.copyElements();
        newElements.remove(node);
        return new ParsedFeatures(this.getFilename(), this.getLineNumber(), newElements);
    }

    public void acceptVisitor(ParsedProgramVisitor visitor) {
        visitor.visitFeaturesPreorder(this);
        final Iterator<ParsedFeature> iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().acceptVisitor(visitor);
            if (iterator.hasNext()) {
                visitor.visitFeaturesInorder(this);
            }
        }
        visitor.visitFeaturesPostorder(this);
    }
}