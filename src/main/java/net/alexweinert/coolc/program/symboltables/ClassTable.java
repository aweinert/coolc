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

// This is a project skeleton file

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.Class;
import net.alexweinert.coolc.program.ast.Classes;
import net.alexweinert.coolc.program.ast.Features;
import net.alexweinert.coolc.program.ast.Formal;
import net.alexweinert.coolc.program.ast.Formals;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.ast.NoExpression;
import net.alexweinert.coolc.program.ast.TreeNode;

/**
 * This class may be used to contain the semantic information such as the inheritance graph. You may use it or not as
 * you like: it is only here to provide a container for the supplied methods.
 */
public class ClassTable {
    private int semantErrors = 0;
    private PrintStream errorStream = System.err;

    private Map<AbstractSymbol, Class> classes = new HashMap<>();

    /**
     * Creates data structures representing basic Cool classes (Object, IO, Int, Bool, String). Please note: as is this
     * method does not do anything useful; you will need to edit it to make if do what you want.
     * */
    private void installBasicClasses() {
        AbstractSymbol filename = AbstractTable.stringtable.addString("<basic class>");

        Class objectClass = createObjectClass(filename);
        this.classes.put(objectClass.getName(), objectClass);

        Class ioClass = createIoClass(filename);
        this.classes.put(ioClass.getName(), ioClass);

        Class intClass = createIntClass(filename);
        this.classes.put(intClass.getName(), intClass);

        Class boolClass = createBoolClass(filename);
        this.classes.put(boolClass.getName(), boolClass);

        Class strClass = createStringClass(filename);
        this.classes.put(strClass.getName(), strClass);
    }

    private Class createStringClass(AbstractSymbol filename) {
        // The class Str has a number of slots and operations:
        // val the length of the string
        // str_field the string itself
        // length() : Int returns length of the string
        // concat(arg: Str) : Str performs string concatenation
        // substr(arg: Int, arg2: Int): Str substring selection

        Class Str_class = new Class(
                0,
                TreeConstants.Str,
                TreeConstants.Object_,
                new Features(0)
                        .add(new Attribute(0, TreeConstants.val, TreeConstants.Int, new NoExpression(0)))
                        .add(new Attribute(0, TreeConstants.str_field, TreeConstants.prim_slot, new NoExpression(0)))
                        .add(new Method(0, TreeConstants.length, new Formals(0), TreeConstants.Int, new NoExpression(0)))
                        .add(new Method(0, TreeConstants.concat, new Formals(0).add(new Formal(0, TreeConstants.arg,
                                TreeConstants.Str)), TreeConstants.Str, new NoExpression(0)))
                        .add(new Method(0, TreeConstants.substr, new Formals(0).add(
                                new Formal(0, TreeConstants.arg, TreeConstants.Int)).add(
                                new Formal(0, TreeConstants.arg2, TreeConstants.Int)), TreeConstants.Str,
                                new NoExpression(0))), filename);
        return Str_class;
    }

    private Class createBoolClass(AbstractSymbol filename) {
        // Bool also has only the "val" slot.
        Class Bool_class = new Class(0, TreeConstants.Bool, TreeConstants.Object_, new Features(0).add(new Attribute(0,
                TreeConstants.val, TreeConstants.prim_slot, new NoExpression(0))), filename);
        return Bool_class;
    }

    private Class createIntClass(AbstractSymbol filename) {
        // The Int class has no methods and only a single attribute, the
        // "val" for the integer.

        Class Int_class = new Class(0, TreeConstants.Int, TreeConstants.Object_, new Features(0).add(new Attribute(0,
                TreeConstants.val, TreeConstants.prim_slot, new NoExpression(0))), filename);
        return Int_class;
    }

    private Class createIoClass(AbstractSymbol filename) {
        // The IO class inherits from Object. Its methods are
        // out_string(Str) : SELF_TYPE writes a string to the output
        // out_int(Int) : SELF_TYPE "    an int    " "     "
        // in_string() : Str reads a string from the input
        // in_int() : Int "   an int     " "     "

        Class IO_class = new Class(0, TreeConstants.IO, TreeConstants.Object_, new Features(0)
                .add(new Method(0, TreeConstants.out_string, new Formals(0).add(new Formal(0, TreeConstants.arg,
                        TreeConstants.Str)), TreeConstants.SELF_TYPE, new NoExpression(0)))
                .add(new Method(0, TreeConstants.out_int, new Formals(0).add(new Formal(0, TreeConstants.arg,
                        TreeConstants.Int)), TreeConstants.SELF_TYPE, new NoExpression(0)))
                .add(new Method(0, TreeConstants.in_string, new Formals(0), TreeConstants.Str, new NoExpression(0)))
                .add(new Method(0, TreeConstants.in_int, new Formals(0), TreeConstants.Int, new NoExpression(0))),
                filename);
        return IO_class;
    }

    private Class createObjectClass(AbstractSymbol filename) {
        // The following demonstrates how to create dummy parse trees to
        // refer to basic Cool classes. There's no need for method
        // bodies -- these are already built into the runtime system.

        // IMPORTANT: The results of the following expressions are
        // stored in local variables. You will want to do something
        // with those variables at the end of this method to make this
        // code meaningful.

        // The Object class has no parent class. Its methods are
        // cool_abort() : Object aborts the program
        // type_name() : Str returns a string representation
        // of class name
        // copy() : SELF_TYPE returns a copy of the object

        Class Object_class = new Class(0, TreeConstants.Object_, TreeConstants.No_class,
                new Features(0)
                        .add(new Method(0, TreeConstants.cool_abort, new Formals(0), TreeConstants.Object_,
                                new NoExpression(0)))
                        .add(new Method(0, TreeConstants.type_name, new Formals(0), TreeConstants.Str,
                                new NoExpression(0)))
                        .add(new Method(0, TreeConstants.copy, new Formals(0), TreeConstants.SELF_TYPE,
                                new NoExpression(0))), filename);
        return Object_class;
    }

    /**
     * Creates the classtable, includes both the basic classes and the given classes. If a class of the same name
     * appears twice or more in cls, an error is reported.
     * 
     * @param cls
     *            The classes to include in this table in addition to basic classes
     */
    public ClassTable(Classes cls) {
        // Add Object, String, Int, Bool, IO
        this.installBasicClasses();

        // Walk through all the classes, add them one by one
        for (Class currentClass : cls) {
            if (this.classes.containsKey(currentClass.getName())) {
                // If this is a redefinition, report the error and ignore the redefinition
                String errorString = String.format("Redefinition of class %s.", currentClass.getName());
                PrintStream errorStream = this.semantError((Class) currentClass);
                errorStream.println(errorString);

            } else {
                this.classes.put(currentClass.getName(), currentClass);
            }
        }
    }

    public Class getClass(AbstractSymbol symbol) {
        return this.classes.get(symbol);
    }

    /**
     * @return True if the given class exists in the program
     */
    public boolean classExists(AbstractSymbol symbol) {
        return this.classes.containsKey(symbol);
    }

    /**
     * @return All the classes defined in the program, including the basic classes
     */
    public Iterable<Class> getClasses() {
        return this.classes.values();
    }

    /**
     * @return True iff child <= parent. Either one or both may be SELF_TYPE.
     */
    public boolean conformsTo(AbstractSymbol enclosingClass, AbstractSymbol child, AbstractSymbol parent) {
        if (child.equals(TreeConstants.No_type)) {
            // No_Type conforms to all types
            return true;
        }

        if (child.equals(TreeConstants.SELF_TYPE)) {
            child = enclosingClass;
        }

        if (parent.equals(TreeConstants.SELF_TYPE)) {
            parent = enclosingClass;
        }

        Collection<AbstractSymbol> visited = new HashSet<>();

        Class currentAncestor = (Class) this.getClass(child);

        // Continue until we either find the parent or get stuck in a loop
        // TODO: Do not need to check for loop, if this takes too long
        while (!(isNoClass(currentAncestor) || currentAncestor.getName().equals(parent) || visited
                .contains(currentAncestor.getName()))) {
            visited.add(currentAncestor.getName());
            if (currentAncestor.getName().equals(TreeConstants.Object_)) {
                // Stop if we are at object, going further would lead us into null-pointer-dereferences
                break;
            }
            currentAncestor = (Class) this.getClass(currentAncestor.getParent());
        }

        if (currentAncestor.getName().equals(parent)) {
            return true;
        }
        return false;
    }

    private boolean isNoClass(Class candidateClass) {
        return candidateClass.getName().equals(TreeConstants.No_class);
    }

    /**
     * Prints line number and file name of the given class.
     * 
     * Also increments semantic error count.
     * 
     * @param c
     *            the class
     * @return a print stream to which the rest of the error message is to be printed.
     * 
     * */
    public PrintStream semantError(Class c) {
        return semantError(c.getFilename(), c);
    }

    /**
     * Prints the file name and the line number of the given tree node.
     * 
     * Also increments semantic error count.
     * 
     * @param filename
     *            the file name
     * @param t
     *            the tree node
     * @return a print stream to which the rest of the error message is to be printed.
     * 
     * */
    public PrintStream semantError(AbstractSymbol filename, TreeNode t) {
        errorStream.print(filename + ":" + t.getLineNumber() + ": ");
        return semantError();
    }

    /**
     * Increments semantic error count and returns the print stream for error messages.
     * 
     * @return a print stream to which the error message is to be printed.
     * 
     * */
    public PrintStream semantError() {
        semantErrors++;
        return errorStream;
    }

    /** Returns true if there are any static semantic errors. */
    public boolean errors() {
        return semantErrors != 0;
    }

    /**
     * @return The least upper bound of className1 and className2, i.e., the most specific class c such that className1
     *         <= c and className2 <= c.
     */
    public AbstractSymbol getLeastUpperBound(AbstractSymbol className1, AbstractSymbol className2) {
        // First, collect all ancestors of className1
        Collection<AbstractSymbol> predecessors1 = new LinkedList<>();
        AbstractSymbol currentPredecessor1 = className1;
        do {
            predecessors1.add(currentPredecessor1);
            currentPredecessor1 = this.getClass(currentPredecessor1).getParent();
        } while (!currentPredecessor1.equals(TreeConstants.No_class));

        // Now, walk through the predecessors of className2 and check when we meet a predecessor of className1
        AbstractSymbol currentPredecessor2 = className2;
        while (!predecessors1.contains(currentPredecessor2)) {
            currentPredecessor2 = this.getClass(currentPredecessor2).getParent();
        }

        return currentPredecessor2;
    }

    /**
     * @param currentClass
     *            The class for which to get all parents
     * @return An Iterable that iterates over all the given class' parents, excluding the given class, but up to and
     *         including the Object-class
     */
    public Iterable<Class> getAncestors(Class currentClass) {
        // TODO This value should be cached. Could then also be used in other contexts
        List<Class> returnValue = new LinkedList<>();

        if (currentClass.getName().equals(TreeConstants.Object_)) {
            return returnValue;
        }

        Class currentAncestor = currentClass;

        do {
            currentAncestor = this.getClass(currentAncestor.getParent());
            returnValue.add(currentAncestor);
        } while (!currentAncestor.getName().equals(TreeConstants.Object_));

        return returnValue;
    }

}
