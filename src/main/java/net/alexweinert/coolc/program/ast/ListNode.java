package net.alexweinert.coolc.program.ast;

/* Copyright (c) 2000 The Regents of the University of California. All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software for any purpose, without fee, and without written
 * agreement is hereby granted, provided that the above copyright notice and the following two paragraphs appear in all
 * copies of this software.
 * 
 * IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR
 * CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
 * CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS"
 * BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR
 * MODIFICATIONS. */

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;

import net.alexweinert.coolc.program.Utilities;

/**
 * Base class for lists of AST elements.
 * 
 * <p>
 * 
 * (See <a href="TreeNode.html">TreeNode</a> for a discussion of AST nodes in general)
 * 
 * <p>
 * 
 * List phyla have a distinct set of operations for constructing and accessing lists. For each phylum named <em>X</em>
 * there is a phylum called <em>X</em>s (except for <code>Classes</code>, which is a list of <code>Class_</code> nodes)
 * of type <code>List[X]</code>.
 * 
 * <p>
 * 
 * An empty list is created with <code>new Xs(lineno)</code>. Elements may be appended to the list using either
 * <code>addElement()</code> or <code>appendElement()</code>. <code>appendElement</code> returns the list itself, so
 * calls to it may be chained, as in <code>
    list.appendElement(Foo).appendElement(Bar).appendElement(Baz)</code>.
 * 
 * <p>
 * 
 * You can use <code>java.util.Enumeration</code> to iterate through lists. If you are not familiar with that interface,
 * look it up in the Java API documentation. Here's an example of iterating through a list:
 * 
 * <pre>
 *   for (Enumeration e = list.getElements(); e.hasMoreElements(); ) {
 *     Object n = e.nextElement();
 *     ... do something with n ...
 *   }
 * </pre>
 * 
 * Alternatively, it is possible to iterate using an integer index:
 * 
 * <pre>
 *   for (int i = 0; i < list.getLength(); i++) {
 *     ... do something with list.getNth(i) ...
 *   }
 * </pre>
 * 
 * Note: <code>nextElement()</code> returns the value of type <code>Object</code> and <code>getNth()</code> returns the
 * value of type <code>TreeNode</code>. You will most likely need to cast it to the type appropriate for the list
 * element. <em>This is one of the
    very few cases where casting is actually necessary and
    appropriate</em>.
 */

abstract class ListNode<T extends TreeNode> extends TreeNode implements Iterable<T> {
    private final Vector<T> elements;

    protected ListNode(int lineNumber, Vector<T> elements) {
        super(lineNumber);
        this.elements = elements;
    }

    /**
     * Builds an empty ListNode
     * 
     * @param lineNumber
     *            line in the source file from which this node came.
     * */
    protected ListNode(int lineNumber) {
        super(lineNumber);
        elements = new Vector<>();
    }

    /**
     * Retreives nth element of the list.
     * 
     * @param n
     *            the index of the element
     * @return the element
     * */
    public T getNth(int n) {
        return elements.elementAt(n);
    }

    /**
     * Retreives the length of the list.
     * 
     * @return the length of the list
     * */
    public int getLength() {
        return elements.size();
    }

    /**
     * Retreives the elements of the list as Enumeration.
     * 
     * @return the elements
     * */
    public Iterable<T> getElements() {
        return this.elements;
    }

    /**
     * @param node
     *            a node to append
     * @return A new ListNode with the given node appended
     * */
    public abstract ListNode<T> addElement(T node);

    /**
     * Pretty-prints this list to this output stream.
     * 
     * @param out
     *            the output stream
     * @param n
     *            the number of spaces to indent the output
     * */
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n));
        out.print("list\n");
        for (int i = 0; i < getLength(); i++) {
            getNth(i).dump(out, n + 2);
        }
        out.print(Utilities.pad(n));
        out.print("(end_of_list)\n");
    }

    /**
     * Returns a string representation of this list.
     * 
     * @return a string representation
     * */
    public String toString() {
        return elements.toString();
    }
}
