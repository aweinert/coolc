package net.alexweinert.coolc.representations.cool.program.parsed;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.alexweinert.coolc.representations.cool.expressions.untyped.ExpressionVisitor;
import net.alexweinert.coolc.representations.cool.util.ListNode;

/**
 * Defines list phylum Classes
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
public class Classes extends ListNode<ClassNode> {

    public Classes(String filename, int lineNumber, Collection<ClassNode> elements) {
        super(filename, lineNumber, elements);
    }

    /** Creates an empty "Classes" list */
    public Classes(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    @Override
    public Classes add(ClassNode node) {
        final Collection<ClassNode> newElements = this.copyElements();
        newElements.add(node);
        return new Classes(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public Classes remove(ClassNode node) {
        final Collection<ClassNode> newElements = this.copyElements();
        newElements.remove(node);
        return new Classes(this.getFilename(), this.getLineNumber(), newElements);
    }

    /**
     * Returns a new list in which the first occurrence of oldNode is replaced with newNode. If oldNode does not occur
     * in this list, the list is returned
     */
    public Classes replace(ClassNode oldNode, ClassNode newNode) {
        final List<ClassNode> newElements = this.copyElements();
        for (int i = 0; i < newElements.size(); ++i) {
            if (newElements.get(i).equals(oldNode)) {
                newElements.set(i, newNode);
                return new Classes(this.getFilename(), this.getLineNumber(), newElements);
            }
        }
        return this;
    }

    public void acceptVisitor(ParsedProgramVisitor visitor) {
        visitor.visitClassesPreorder(this);
        final Iterator<ClassNode> iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().acceptVisitor(visitor);
            if (iterator.hasNext()) {
                visitor.visitClassesInorder(this);
            }
        }
        visitor.visitClassesPostorder(this);
    }
}