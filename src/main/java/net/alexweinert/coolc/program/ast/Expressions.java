package net.alexweinert.coolc.program.ast;

import java.util.Vector;

/**
 * Defines list phylum Expressions
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
public class Expressions extends ListNode {
    public final static Class elementClass = Expression.class;

    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }

    protected Expressions(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /** Creates an empty "Expressions" list */
    public Expressions(int lineNumber) {
        super(lineNumber);
    }

    /** Appends "Expression" element to this list */
    public Expressions appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Expressions(lineNumber, copyElements());
    }
}