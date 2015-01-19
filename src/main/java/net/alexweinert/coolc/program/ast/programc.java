package net.alexweinert.coolc.program.ast;

/**
 * Defines AST constructor 'programc'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class programc extends Program {
    protected Classes classes;

    /**
     * Creates "programc" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for classes
     */
    public programc(int lineNumber, Classes a1) {
        super(lineNumber);
        classes = a1;
    }

    public TreeNode copy() {
        return new programc(lineNumber, (Classes) classes.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "programc\n");
        classes.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_program");
        for (Enumeration e = classes.getElements(); e.hasMoreElements();) {
            ((Class_) e.nextElement()).dump_with_types(out, n + 2);
        }
    }

    /**
     * This method is the entry point to the semantic checker. You will need to complete it in programming assignment 4.
     * <p>
     * Your checker should do the following two things:
     * <ol>
     * <li>Check that the program is semantically correct
     * <li>Decorate the abstract syntax tree with type information by setting the type field in each Expression node.
     * (see tree.h)
     * </ol>
     * <p>
     * You are free to first do (1) and make sure you catch all semantic errors. Part (2) can be done in a second stage
     * when you want to test the complete compiler.
     */
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
        for (Class_ currentClass : classTable.getClasses()) {
            AbstractSymbol inheritedBaseClass = null;
            if (((class_c) currentClass).getParent().equals(TreeConstants.Int)) {
                inheritedBaseClass = TreeConstants.Int;
            } else if (((class_c) currentClass).getParent().equals(TreeConstants.Bool)) {
                inheritedBaseClass = TreeConstants.Bool;
            } else if (((class_c) currentClass).getParent().equals(TreeConstants.Str)) {
                inheritedBaseClass = TreeConstants.Str;
            }

            if (inheritedBaseClass != null) {
                String errorString = String.format("Class %s cannot inherit class %s", currentClass.getName(),
                        TreeConstants.Int);
                classTable.semantError((class_c) currentClass).println(errorString);
            }
        }
    }

    private void checkParentExistence(ClassTable classTable) {
        for (Class_ currentClass : classTable.getClasses()) {
            /* Skip the object-class, since this one is allowed to have a non existing parent */
            if (currentClass.getName().equals(TreeConstants.Object_)) {
                continue;
            }

            if (!classTable.classExists(currentClass.getParent())) {
                String errorString = String.format("Class %s inherits from an undefined class %s",
                        currentClass.getName(), currentClass.getParent());
                classTable.semantError((class_c) currentClass).println(errorString);
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
        for (Class_ definedClass : classTable.getClasses()) {
            if (!reachable.contains(definedClass.getName())) {
                String errorString = String.format(
                        "Class %s, or an ancestor of %s, is involved in an inheritance cycle.", definedClass.getName(),
                        definedClass.getName());
                classTable.semantError((class_c) definedClass).println(errorString);
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
                classTable.semantError((class_c) classTable.getClass(currentToCheck)).println(errorString);
            } else {
                visited.add(currentToCheck);

                // Add all of currentToCheck's children
                // TODO Cache the children of all classes for performance reasons
                for (Class_ potentialChild : classTable.getClasses()) {
                    if (potentialChild.getParent().equals(currentToCheck)) {
                        toCheck.add(potentialChild.getName());
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
        for (int i = 0; i < this.classes.getLength(); ++i) {
            class_c currentClass = (class_c) this.classes.getNth(i);
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
                classTable.semantError((class_c) classTable.getClass(TreeConstants.Main)).println(errorString);
            } else {
                FeatureTable.MethodSignature mainSignature = featureTable.getMethodSignatures(TreeConstants.Main).get(
                        TreeConstants.main_meth);
                // Check that Main.main takes no arguments
                if (mainSignature.getArgumentTypes().size() != 0) {
                    String errorString = "'main' method in class Main should have no arguments.";
                    classTable.semantError((class_c) classTable.getClass(TreeConstants.Main)).println(errorString);
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

}