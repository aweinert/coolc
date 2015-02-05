package net.alexweinert.coolc.program.symboltables;

import net.alexweinert.coolc.program.Utilities;

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

public class IdTable extends AbstractTable<String> {
    private static final IdTable instance = new IdTable();

    public static IdTable getInstance() {
        return IdTable.instance;
    }

    /**
     * Creates a new IdSymbol object.
     * 
     * @see IdSymbol
     * */
    protected IdSymbol getNewSymbol(String s, int index) {
        return new IdSymbol(s, index);
    }

    /**
     * Adds the specified string to this string table
     *
     * @param s
     *            the string to add
     * @return the symbol for the string s
     * */
    public IdSymbol addString(String s) {
        if (!this.tbl.containsKey(s)) {
            this.tbl.put(s, getNewSymbol(s, tbl.size()));
        }
        return (IdSymbol) this.tbl.get(s);
    }
}
