package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * The complete program. Root node of a well-formed AST
 */
public class Program extends TreeNode {
    protected final Classes classes;

    /**
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param classes
     *            initial value for classes
     */
    public Program(int lineNumber, Classes classes) {
        super(lineNumber);
        this.classes = classes;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "program\n");
        classes.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_program");
        for (Class currentClass : this.classes) {
            currentClass.dump_with_types(out, n + 2);
        }
    }

    /** Checks the program for semantic correctness */
    public void semant() {
        // Construct the class tree and check for errors in the form of it
        ClassTable classTable = new ClassTable(classes);
        this.checkInheritanceTree(classTable);

        // We may abort here if the inheritance is screwed up
        this.abortIfError(classTable);

        // Now we do the type
        this.typecheck(classTable);

        this.abortIfError(classTable);
    }

    private void checkInheritanceTree(ClassTable classTable) {
        this.checkParentExistence(classTable);
        this.checkTreeForm(classTable);
        this.checkBasicClasses(classTable);
    }

    private void checkBasicClasses(ClassTable classTable) {
        for (Class currentClass : classTable.getClasses()) {
            AbstractSymbol inheritedBaseClass = null;
            if (((Class) currentClass).getParent().equals(TreeConstants.Int)) {
                inheritedBaseClass = TreeConstants.Int;
            } else if (((Class) currentClass).getParent().equals(TreeConstants.Bool)) {
                inheritedBaseClass = TreeConstants.Bool;
            } else if (((Class) currentClass).getParent().equals(TreeConstants.Str)) {
                inheritedBaseClass = TreeConstants.Str;
            }

            if (inheritedBaseClass != null) {
                String errorString = String.format("Class %s cannot inherit class %s", currentClass.getIdentifier(),
                        TreeConstants.Int);
                classTable.semantError((Class) currentClass).println(errorString);
            }
        }
    }

    private void checkParentExistence(ClassTable classTable) {
        for (Class currentClass : classTable.getClasses()) {
            /* Skip the object-class, since this one is allowed to have a non existing parent */
            if (currentClass.getIdentifier().equals(TreeConstants.Object_)) {
                continue;
            }

            if (!classTable.classExists(currentClass.getParent())) {
                String errorString = String.format("Class %s inherits from an undefined class %s",
                        currentClass.getIdentifier(), currentClass.getParent());
                classTable.semantError((Class) currentClass).println(errorString);
            }
        }
    }

    private void checkTreeForm(ClassTable classTable) {
        Collection<AbstractSymbol> reachable = this.checkSinglePath(classTable);
        this.checkReachability(classTable, reachable);
    }

    /**
     * Checks that every class that is defined in the given classTable is also in the given set of classes that are
     * reachable from Object
     * 
     * @param classTable
     *            The table containing all classes defined in the program
     * @param reachable
     *            The set of classes that are reachable from Object via the inheritance hierarchy
     */
    private void checkReachability(ClassTable classTable, Collection<AbstractSymbol> reachable) {
        // Check that all defined classes are reachable from Object
        for (Class definedClass : classTable.getClasses()) {
            if (!reachable.contains(definedClass.getIdentifier())) {
                String errorString = String.format(
                        "Class %s, or an ancestor of %s, is involved in an inheritance cycle.", definedClass.getIdentifier(),
                        definedClass.getIdentifier());
                classTable.semantError((Class) definedClass).println(errorString);
            }
        }
    }

    /**
     * Checks that there is only a single path from Object to every AbstractSymbol that is reachable from it. If this is
     * the case, then Object is the root of a (inheritance) tree
     * 
     * @param classTable
     *            The table of all classes
     * @return The set of all classes that conform to Object
     */
    private Collection<AbstractSymbol> checkSinglePath(ClassTable classTable) {
        Collection<AbstractSymbol> visited = new HashSet<>();
        Queue<AbstractSymbol> toCheck = new LinkedBlockingQueue<>();

        toCheck.add(TreeConstants.Object_);

        while (!toCheck.isEmpty()) {
            AbstractSymbol currentToCheck = toCheck.poll();
            if (visited.contains(currentToCheck)) {
                String errorString = String.format("%s is reachable from two parents", currentToCheck);
                classTable.semantError((Class) classTable.getClass(currentToCheck)).println(errorString);
            } else {
                visited.add(currentToCheck);

                // Add all of currentToCheck's children
                // TODO Cache the children of all classes for performance reasons
                for (Class potentialChild : classTable.getClasses()) {
                    if (potentialChild.getParent().equals(currentToCheck)) {
                        toCheck.add(potentialChild.getIdentifier());
                    }
                }
            }
        }
        return visited;
    }

    private void typecheck(ClassTable classTable) {
        // Construct the FeatureTable for all classes. This also checks for redefinition errors
        FeatureTable featureTable = new FeatureTable(classTable);

        checkForMainMethod(classTable, featureTable);

        // Typecheck each class on its own
        for (final Class currentClass : this.classes) {
            currentClass.typecheck(classTable, featureTable);
        }
    }

    /**
     * Checks that Main.main exists and has the correct type. Records an error otherwise.
     */
    private void checkForMainMethod(ClassTable classTable, FeatureTable featureTable) {
        // Check that class Main exists
        if (!classTable.classExists(TreeConstants.Main)) {
            classTable.semantError().println("Class Main is not defined.");
        } else {
            // Check that class Main has method 'main'
            if (!featureTable.getMethodSignatures(TreeConstants.Main).containsKey(TreeConstants.main_meth)) {
                String errorString = "No 'main' method in class Main";
                classTable.semantError((Class) classTable.getClass(TreeConstants.Main)).println(errorString);
            } else {
                FeatureTable.MethodSignature mainSignature = featureTable.getMethodSignatures(TreeConstants.Main).get(
                        TreeConstants.main_meth);
                // Check that Main.main takes no arguments
                if (mainSignature.getArgumentTypes().size() != 0) {
                    String errorString = "'main' method in class Main should have no arguments.";
                    classTable.semantError((Class) classTable.getClass(TreeConstants.Main)).println(errorString);
                }
            }
        }
    }

    private void abortIfError(ClassTable classTable) {
        if (classTable.errors()) {
            System.err.println("Compilation halted due to static semantic errors.");
            System.exit(1);
        }
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitProgramPreorder(this);
        this.classes.acceptVisitor(visitor);
        visitor.visitProgramPostorder(this);
    }

}