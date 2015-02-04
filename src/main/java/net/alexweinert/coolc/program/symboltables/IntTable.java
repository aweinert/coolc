package net.alexweinert.coolc.program.symboltables;

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

public class IntTable extends AbstractTable {
    /**
     * Creates a new IntSymbol object.
     * 
     * @see IntSymbol
     * */
    protected AbstractSymbol getNewSymbol(String s, int len, int index) {
        return new IntSymbol(s, len, index);
    }

    /**
     * Adds the string representation of the specified integer to this string table
     *
     * @param i
     *            the integer to add
     * @return the symbol for the integer i
     * */
    public AbstractSymbol addInt(int i) {
        final String s = Integer.toString(i);
        int len = Math.min(s.length(), MAXSIZE);
        AbstractSymbol sym = null;
        for (int index = 0; index < tbl.size(); index++) {
            try {
                sym = (AbstractSymbol) tbl.elementAt(index);
            } catch (ArrayIndexOutOfBoundsException ex) {
                Utilities.fatalError("Unexpected exception: " + ex);
            }
            if (sym.equalString(s, len)) {
                return sym;
            }
        }
        sym = getNewSymbol(s, len, tbl.size());
        tbl.addElement(sym);
        return sym;
    }

    public AbstractSymbol addInt(String s) {
        return this.addInt(Integer.parseInt(s));
    }

    /**
     * Generates code for all int constants in the int table.
     * 
     * @param intclasstag
     *            the class tag for Int
     * @param s
     *            the output stream
     * */
    public void codeStringTable(int intclasstag, PrintStream s) {
        IntSymbol sym = null;
        for (int i = tbl.size() - 1; i >= 0; i--) {
            try {
                sym = (IntSymbol) tbl.elementAt(i);
            } catch (ArrayIndexOutOfBoundsException ex) {
                Utilities.fatalError("Unexpected exception: " + ex);
            }
            sym.codeDef(intclasstag, s);
        }
    }
}
