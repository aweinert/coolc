package net.alexweinert.coolc.program.ast;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;

/**
 * Defines list phylum Classes
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
public class Classes extends ListNode<Class> {

    public Classes(String filename, int lineNumber, Collection<Class> elements) {
        super(filename, lineNumber, elements);
    }

    /** Creates an empty "Classes" list */
    public Classes(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    @Override
    public Classes add(Class node) {
        final Collection<Class> newElements = this.copyElements();
        newElements.add(node);
        return new Classes(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public Classes remove(Class node) {
        final Collection<Class> newElements = this.copyElements();
        newElements.remove(node);
        return new Classes(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitClassesPreorder(this);
        final Iterator<Class> iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().acceptVisitor(visitor);
            if (iterator.hasNext()) {
                visitor.visitClassesInorder(this);
            }
        }
        visitor.visitClassesPostorder(this);
    }
}