package net.alexweinert.coolc.representations.cool.util;

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

import net.alexweinert.coolc.representations.cool.Utilities;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IntSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.StringSymbol;

public abstract class TreeNode {
    /** the name of the file this node came from */
    private final String filename;
    /** line in the source file from which this node came. */
    private final int lineNumber;

    /**
     * Builds a new tree node
     *
     * @param lineNumber
     *            The line in the source file from which this node came.
     * */
    protected TreeNode(String filename, int lineNumber) {
        this.filename = filename;
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

    public String getFilename() {
        return this.filename;
    }

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
    protected void dump_IdSymbol(PrintStream out, int n, IdSymbol sym) {
        out.print(Utilities.pad(n));
        out.println(sym.getString());
    }

    /**
     * Dumps a printable representation of an AbstactSymbol value.
     * 
     * This method is used internally by the generated AST classes
     * */
    protected void dump_IntSymbol(PrintStream out, int n, IntSymbol sym) {
        out.print(Utilities.pad(n));
        out.println(sym.getString());
    }

    /**
     * Dumps a printable representation of an AbstactSymbol value.
     * 
     * This method is used internally by the generated AST classes
     * */
    protected void dump_StringSymbol(PrintStream out, int n, StringSymbol sym) {
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

    public abstract void acceptVisitor(Visitor visitor);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((filename == null) ? 0 : filename.hashCode());
        result = prime * result + lineNumber;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TreeNode other = (TreeNode) obj;
        if (filename == null) {
            if (other.filename != null) {
                return false;
            }
        } else if (!filename.equals(other.filename)) {
            return false;
        }
        if (lineNumber != other.lineNumber) {
            return false;
        }
        return true;
    }
}
