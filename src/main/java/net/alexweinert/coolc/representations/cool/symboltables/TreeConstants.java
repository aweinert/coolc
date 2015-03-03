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

// This is a project skeleton file

/**
 * For convenience, this clas defines a large number of symbols. These symbols include the primitive type and method
 * names, as well as fixed names used by the runtime system.
 */
public class TreeConstants {
    public static final IdSymbol arg = IdTable.getInstance().addString("arg");

    public static final IdSymbol arg2 = IdTable.getInstance().addString("arg2");

    public static final IdSymbol Bool = IdTable.getInstance().getBoolSymbol();

    public static final IdSymbol concat = IdTable.getInstance().addString("concat");

    public static final IdSymbol cool_abort = IdTable.getInstance().addString("abort");

    public static final IdSymbol copy = IdTable.getInstance().addString("copy");

    public static final IdSymbol Int = IdTable.getInstance().getIntSymbol();

    public static final IdSymbol in_int = IdTable.getInstance().addString("in_int");

    public static final IdSymbol in_string = IdTable.getInstance().addString("in_string");

    public static final IdSymbol IO = IdTable.getInstance().getIOSymbol();

    public static final IdSymbol length = IdTable.getInstance().addString("length");

    public static final IdSymbol Main = IdTable.getInstance().addString("Main");

    public static final IdSymbol main_meth = IdTable.getInstance().addString("main");

    public static final IdSymbol No_class = IdTable.getInstance().addString("_no_class");

    public static final IdSymbol No_type = IdTable.getInstance().addString("_no_type");

    public static final IdSymbol Object_ = IdTable.getInstance().getObjectSymbol();

    public static final IdSymbol out_int = IdTable.getInstance().addString("out_int");

    public static final IdSymbol out_string = IdTable.getInstance().addString("out_string");

    public static final IdSymbol prim_slot = IdTable.getInstance().addString("_prim_slot");

    public static final IdSymbol self = IdTable.getInstance().addString("self");

    public static final IdSymbol SELF_TYPE = IdTable.getInstance().addString("SELF_TYPE");

    public static final IdSymbol Str = IdTable.getInstance().getStringSymbol();

    public static final IdSymbol str_field = IdTable.getInstance().addString("_str_field");

    public static final IdSymbol substr = IdTable.getInstance().addString("substr");

    public static final IdSymbol type_name = IdTable.getInstance().addString("type_name");

    public static final IdSymbol val = IdTable.getInstance().addString("_val");
}
