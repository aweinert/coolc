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

import java.util.HashMap;
import java.util.Map;

import net.alexweinert.coolc.program.Utilities;

/**
 * Abstract string table implementation.
 * 
 * <p>
 * 
 * All compilers manage large numbers of strings such as program identifiers, numerical constants, and string constants.
 * Often, many of these strings are the same. For example, each identifier typically occurs many times in a program. To
 * ensure that string constants are stored compactly and manipulated efficiently, a specialized data structure, the
 * <em>string table</em>, is employed.
 * 
 * <p>
 * 
 * 
 * A string table is a lookup table that maintains a single copy of each string. The Cool string table class provides
 * methods for inserting and querying string tables in a variety of ways. While production compilers use hashed data
 * structures to implement string tables, the Cool string tables are implemented as vectors. The components of Cool
 * string tables are of type <a href="AbstractSymbol.html"> AbstractSymbol</a>). Each AbstractSymbol stores a string,
 * and an integer index unique to the string.
 * 
 * <p>
 * 
 * An important point about the structure of the Cool compiler is that there are actually three distinct string tables:
 * one for string constants (<a href="#stringtable">StringTable.getInstance()</a>), one for integer constants (<a
 * href="#inttable">IntTable.getInstance()</a>), and one for identifiers (<a href="#idtable">IdTable.getInstance()</a>).
 * The code generator must distinguish integer constants and string constants from each other and from identifiers,
 * because special code is produced for each string constant and each integer constant in the program. Having three
 * distinct string tables makes this distinction easy. Note that each of the three tables has a different element type
 * (<a href="StringSymbol.html">StringSymbol</a>, <a href="IntSymbol.html">IntSymbol</a>, and <a
 * href="IdSymbol.html">IdSymbol</a>), each of which is a derived class of AbstractSymbol.
 * 
 * <p>
 * 
 * Because string tables store only one copy of each string, comparing whether two IntSymbols, StrSymbols, or IdSymbols
 * <tt>x</tt> and <tt>y</tt> represent the same string can be done simply by comparing the two references
 * <tt>x == y</tt>. Note that it does not make sense to compare entries from different string tables (e.g., IdSymbols
 * with StringSymbols as these are guaranteed to be different even if the strings are the same.
 * 
 * @see AbstractSymbol
 * @see StringSymbol
 * @see IdSymbol
 * @see IntSymbol
 */
abstract class AbstractTable<T> {
    /** Vector of table entries */
    protected Map<T, AbstractSymbol<T>> tbl = new HashMap<>();

    /** Creates a new symbol of the appropriate type */
    protected abstract AbstractSymbol<T> getNewSymbol(String s, int index);

    /**
     * Looks up a symbol in this string table by its string representation
     * 
     * A fatal error is signalled if the string is not in the table
     *
     * @param s
     *            the string representation of the symbol
     * @return a symbol corresponding to the string
     * */
    public AbstractSymbol<T> lookup(String s) {
        AbstractSymbol<T> sym = null;
        if (this.tbl.containsKey(s)) {
            return this.tbl.get(s);
        } else {
            Utilities.fatalError("String table lookup failed on string: " + s);
            return null;
        }
    }

    /** Produces a printable representation of the string table */
    public String toString() {
        return this.tbl.toString();
    }
}
