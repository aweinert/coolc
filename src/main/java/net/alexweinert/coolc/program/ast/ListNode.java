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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import net.alexweinert.coolc.program.Utilities;

abstract class ListNode<T extends TreeNode> extends TreeNode implements Iterable<T> {
    /**
     * The actual elements contained in this node
     */
    protected final List<T> elements;

    protected ListNode(int lineNumber, Collection<T> elements) {
        super(lineNumber);
        this.elements = new LinkedList<>(elements);
    }

    protected ListNode(int lineNumber) {
        super(lineNumber);
        elements = new Vector<>();
    }

    @Override
    public Iterator<T> iterator() {
        return ListNode.this.elements.iterator();
    }

    protected Collection<T> copyElements() {
        return new LinkedList<T>(this.elements);
    }

    /**
     * @param node
     *            a node to append
     * @return A new ListNode with the given node appended
     * */
    public abstract ListNode<T> addElement(T node);

    public int size() {
        return this.elements.size();
    }

    public T get(int index) {
        return this.elements.get(index);
    }

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
        for (T currentElement : this.elements) {
            currentElement.dump(out, n + 2);
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
