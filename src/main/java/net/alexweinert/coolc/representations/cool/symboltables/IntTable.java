package net.alexweinert.coolc.representations.cool.symboltables;

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

public class IntTable extends AbstractTable<Integer> {
    private static final IntTable instance = new IntTable();

    public static IntTable getInstance() {
        return instance;
    }

    /**
     * Creates a new IntSymbol object.
     * 
     * @see IntSymbol
     * */
    protected IntSymbol getNewSymbol(String s, int index) {
        return new IntSymbol(s, index);
    }

    /**
     * Adds the string representation of the specified integer to this string table
     *
     * @param i
     *            the integer to add
     * @return the symbol for the integer i
     * */
    public IntSymbol addInt(int i) {
        if (!this.tbl.containsKey(i)) {
            this.tbl.put(i, getNewSymbol(Integer.toString(i), tbl.size()));
        }
        return (IntSymbol) this.tbl.get(i);
    }

    public IntSymbol addInt(String s) {
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
        for (AbstractSymbol<Integer> sym : this.tbl.values()) {
            ((IntSymbol) sym).codeDef(intclasstag, s);
        }
    }
}
