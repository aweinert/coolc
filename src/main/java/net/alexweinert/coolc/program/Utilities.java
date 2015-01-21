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
package net.alexweinert.coolc.program;

import java.io.PrintStream;

import net.alexweinert.coolc.parser.Tokens;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.AbstractTable;
import java_cup.runtime.Symbol;

public class Utilities {
    // change this to true to enable table checking
    private static final boolean checkTables = false;

    // sm: fixed an off-by-one error here; code assumed there were 80 spaces, but
    // in fact only 79 spaces were there; I've made it 80 now
    // 1 2 3 4 5 6 7
    // 01234567890123456789012345678901234567890123456789012345678901234567890123456789
    private static String padding = "                                                                                "; // 80
                                                                                                                        // spaces
                                                                                                                        // for
                                                                                                                        // padding

    /**
     * Prints error message and exits
     *
     * @param msg
     *            the error message
     * */
    public static void fatalError(String msg) {
        (new Throwable(msg)).printStackTrace();
        System.exit(1);
    }

    /**
     * Prints an appropritely escaped string
     * 
     * @param str
     *            the output stream
     * @param s
     *            the string to print
     * */
    public static void printEscapedString(PrintStream str, String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
            case '\\':
                str.print("\\\\");
                break;
            case '\"':
                str.print("\\\"");
                break;
            case '\n':
                str.print("\\n");
                break;
            case '\t':
                str.print("\\t");
                break;
            case '\b':
                str.print("\\b");
                break;
            case '\f':
                str.print("\\f");
                break;
            default:
                if (c >= 0x20 && c <= 0x7f) {
                    str.print(c);
                } else {
                    String octal = Integer.toOctalString(c);
                    str.print('\\');
                    switch (octal.length()) {
                    case 1:
                        str.print('0');
                    case 2:
                        str.print('0');
                    default:
                        str.print(octal);
                    }
                }
            }
        }
    }

    /**
     * Returns a string representation for a token
     *
     * @param s
     *            the token
     * @return the string representation
     * */
    public static String tokenToString(Symbol s) {
        switch (s.sym) {
        case Tokens.CLASS:
            return ("CLASS");
        case Tokens.ELSE:
            return ("ELSE");
        case Tokens.FI:
            return ("FI");
        case Tokens.IF:
            return ("IF");
        case Tokens.IN:
            return ("IN");
        case Tokens.INHERITS:
            return ("INHERITS");
        case Tokens.LET:
            return ("LET");
        case Tokens.LOOP:
            return ("LOOP");
        case Tokens.POOL:
            return ("POOL");
        case Tokens.THEN:
            return ("THEN");
        case Tokens.WHILE:
            return ("WHILE");
        case Tokens.ASSIGN:
            return ("ASSIGN");
        case Tokens.CASE:
            return ("CASE");
        case Tokens.ESAC:
            return ("ESAC");
        case Tokens.OF:
            return ("OF");
        case Tokens.DARROW:
            return ("DARROW");
        case Tokens.NEW:
            return ("NEW");
        case Tokens.STR_CONST:
            return ("STR_CONST");
        case Tokens.INT_CONST:
            return ("INT_CONST");
        case Tokens.BOOL_CONST:
            return ("BOOL_CONST");
        case Tokens.TYPEID:
            return ("TYPEID");
        case Tokens.OBJECTID:
            return ("OBJECTID");
        case Tokens.ERROR:
            return ("ERROR");
        case Tokens.error:
            return ("ERROR");
        case Tokens.LE:
            return ("LE");
        case Tokens.NOT:
            return ("NOT");
        case Tokens.ISVOID:
            return ("ISVOID");
        case Tokens.PLUS:
            return ("'+'");
        case Tokens.DIV:
            return ("'/'");
        case Tokens.MINUS:
            return ("'-'");
        case Tokens.MULT:
            return ("'*'");
        case Tokens.EQ:
            return ("'='");
        case Tokens.LT:
            return ("'<'");
        case Tokens.DOT:
            return ("'.'");
        case Tokens.NEG:
            return ("'~'");
        case Tokens.COMMA:
            return ("','");
        case Tokens.SEMI:
            return ("';'");
        case Tokens.COLON:
            return ("':'");
        case Tokens.LPAREN:
            return ("'('");
        case Tokens.RPAREN:
            return ("')'");
        case Tokens.AT:
            return ("'@'");
        case Tokens.LBRACE:
            return ("'{'");
        case Tokens.RBRACE:
            return ("'}'");
        case Tokens.EOF:
            return ("EOF");
        default:
            return ("<Invalid Token: " + s.sym + ">");
        }
    }

    /**
     * Prints a token to stderr
     *
     * @param s
     *            the token
     * */
    public static void printToken(Symbol s) {
        System.err.print(tokenToString(s));

        String val = null;

        switch (s.sym) {
        case Tokens.BOOL_CONST:
            System.err.print(" = " + s.value);
            break;
        case Tokens.INT_CONST:
            val = ((AbstractSymbol) s.value).getString();
            System.err.print(" = " + val);
            if (checkTables) {
                AbstractTable.inttable.lookup(val);
            }
            break;
        case Tokens.TYPEID:
        case Tokens.OBJECTID:
            val = ((AbstractSymbol) s.value).getString();
            System.err.print(" = " + val);
            if (checkTables) {
                AbstractTable.idtable.lookup(val);
            }
            break;
        case Tokens.STR_CONST:
            val = ((AbstractSymbol) s.value).getString();
            System.err.print(" = \"");
            printEscapedString(System.err, val);
            System.err.print("\"");
            if (checkTables) {
                AbstractTable.stringtable.lookup(val);
            }
            break;
        case Tokens.ERROR:
            System.err.print(" = \"");
            printEscapedString(System.err, s.value.toString());
            System.err.print("\"");
            break;
        }
        System.err.println("");
    }

    /**
     * Dumps a token to the specified stream
     *
     * @param s
     *            the token
     * @param str
     *            the stream
     * */
    public static void dumpToken(PrintStream str, int lineno, Symbol s) {
        str.print("#" + lineno + " " + tokenToString(s));

        String val = null;

        switch (s.sym) {
        case Tokens.BOOL_CONST:
            str.print(" " + s.value);
            break;
        case Tokens.INT_CONST:
            val = ((AbstractSymbol) s.value).getString();
            str.print(" " + val);
            if (checkTables) {
                AbstractTable.inttable.lookup(val);
            }
            break;
        case Tokens.TYPEID:
        case Tokens.OBJECTID:
            val = ((AbstractSymbol) s.value).getString();
            str.print(" " + val);
            if (checkTables) {
                AbstractTable.idtable.lookup(val);
            }
            break;
        case Tokens.STR_CONST:
            val = ((AbstractSymbol) s.value).getString();
            str.print(" \"");
            printEscapedString(str, val);
            str.print("\"");
            if (checkTables) {
                AbstractTable.stringtable.lookup(val);
            }
            break;
        case Tokens.ERROR:
            str.print(" \"");
            printEscapedString(str, s.value.toString());
            str.print("\"");
            break;
        }

        str.println("");
    }

    /**
     * Returns the specified amount of space padding
     *
     * @param n
     *            the amount of padding
     * */
    public static String pad(int n) {
        if (n > 80)
            return padding;
        if (n < 0)
            return "";
        return padding.substring(0, n);
    }
}
