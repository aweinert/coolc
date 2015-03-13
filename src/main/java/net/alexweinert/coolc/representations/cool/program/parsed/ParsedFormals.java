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
public class ParsedFormals extends ListNode<ParsedFormal> {
    protected ParsedFormals(String filename, int lineNumber, Collection<ParsedFormal> elements) {
        super(filename, lineNumber, elements);
    }

    /** Creates an empty "Formals" list */
    public ParsedFormals(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    @Override
    public ParsedFormals add(ParsedFormal node) {
        final Collection<ParsedFormal> newElements = this.copyElements();
        newElements.add(node);
        return new ParsedFormals(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public ParsedFormals remove(ParsedFormal node) {
        final Collection<ParsedFormal> newElements = this.copyElements();
        newElements.remove(node);
        return new ParsedFormals(this.getFilename(), this.getLineNumber(), newElements);
    }
}