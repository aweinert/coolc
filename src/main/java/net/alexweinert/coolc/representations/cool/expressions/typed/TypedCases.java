package net.alexweinert.coolc.representations.cool.expressions.typed;

import java.util.Collection;
import java.util.Iterator;

import net.alexweinert.coolc.representations.cool.expressions.untyped.ExpressionVisitor;
import net.alexweinert.coolc.representations.cool.util.ListNode;

/**
 * Defines list phylum Cases
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
public class TypedCases extends ListNode<TypedCase> {
    protected TypedCases(String filename, int lineNumber, Collection<TypedCase> elements) {
        super(filename, lineNumber, elements);
    }

    /** Creates an empty "Cases" list */
    public TypedCases(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    @Override
    public TypedCases add(TypedCase node) {
        final Collection<TypedCase> newElements = this.copyElements();
        newElements.add(node);
        return new TypedCases(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public TypedCases remove(TypedCase node) {
        final Collection<TypedCase> newElements = this.copyElements();
        newElements.remove(node);
        return new TypedCases(this.getFilename(), this.getLineNumber(), newElements);
    }

    public void acceptVisitor(TypedExpressionVisitor visitor) {
        visitor.visitCasesPreorder(this);
        final Iterator<TypedCase> iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().acceptVisitor(visitor);
            if (iterator.hasNext()) {
                visitor.visitCasesInorder(this);
            }
        }
        visitor.visitCasesPostorder(this);
    }

}