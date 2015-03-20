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

public class StringTable extends AbstractTable<String> {

    private static final StringTable instance = new StringTable();

    public static StringTable getInstance() {
        return instance;
    }

    /**
     * Creates a new StringSymbol object.
     * 
     * @see StringSymbol
     * */
    protected StringSymbol getNewSymbol(String s, int index) {
        return new StringSymbol(s, index);
    }

    /**
     * Adds prefix of the specified length to this string table
     *
     * @param s
     *            the string to add
     * @return the symbol for the string s
     * */
    public StringSymbol addString(String s) {
        if (!this.tbl.containsKey(s)) {
            this.tbl.put(s, getNewSymbol(s, tbl.size()));
        }
        return (StringSymbol) this.tbl.get(s);
    }
}
