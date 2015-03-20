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

/**
 * This clas encapsulates all aspects of code generation for boolean constatns. String constants and Int constants are
 * handled by StringTable and IntTable respectively, but since there are only two boolean constants, we handle them
 * here.
 */
public class BoolSymbol extends AbstractSymbol<Boolean> {
    private final boolean val;

    /**
     * Creates a new boolean constant.
     * 
     * @param val
     *            the value
     * */
    BoolSymbol(boolean val) {
        super(Boolean.toString(val), val ? 1 : 0);
        this.val = val;
    }

    /**
     * Creates a new boolean constant.
     * 
     * @param val
     *            the value
     * */
    BoolSymbol(Boolean val) {
        this(val.booleanValue());
    }

    final static BoolSymbol truebool = new BoolSymbol(true);
    final static BoolSymbol falsebool = new BoolSymbol(false);
}
