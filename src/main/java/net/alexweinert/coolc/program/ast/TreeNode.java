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

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;

abstract class TreeNode {
    /** line in the source file from which this node came. */
    private final int lineNumber;

    /**
     * Builds a new tree node
     *
     * @param lineNumber
     *            The line in the source file from which this node came.
     * */
    protected TreeNode(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * Retrieves the line number from which this node came.
     *
     * @return the line number
     * */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Pretty-prints this node to this output stream.
     *
     * @param out
     *            the output stream
     * @param n
     *            the number of spaces to indent the output
     * */
    public abstract void dump(PrintStream out, int n);

    /**
     * Dumps a printable representation of a boolean value.
     * 
     * This method is used internally by the generated AST classes
     * */
    protected void dump_Boolean(PrintStream out, int n, Boolean b) {
        out.print(Utilities.pad(n));
        out.println(b.booleanValue() ? "1" : "0");
    }

    /**
     * Dumps a printable representation of an AbstactSymbol value.
     * 
     * This method is used internally by the generated AST classes
     * */
    protected void dump_AbstractSymbol(PrintStream out, int n, AbstractSymbol sym) {
        out.print(Utilities.pad(n));
        out.println(sym.getString());
    }

    /**
     * Dumps a printable representation of current line number
     * 
     * This method is used internally by the generated AST classes
     * */
    protected void dump_line(PrintStream out, int n) {
        out.println(Utilities.pad(n) + "#" + lineNumber);
    }
}
