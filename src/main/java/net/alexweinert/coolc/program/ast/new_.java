package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

/**
 * Defines AST constructor 'new_'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class new_ extends Expression {
    protected AbstractSymbol type_name;

    /**
     * Creates "new_" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for type_name
     */
    public new_(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        type_name = a1;
    }

    public TreeNode copy() {
        return new new_(lineNumber, copy_AbstractSymbol(type_name));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "new_\n");
        dump_AbstractSymbol(out, n + 2, type_name);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_new");
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_type(out, n);
    }

    @Override
    protected AbstractSymbol inferType(Class_ enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        if (classTable.classExists(this.type_name)) {
            return this.type_name;
        } else {
            String errorString = String.format("'new' used with undefined class %s.", this.type_name);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            return TreeConstants.Object_;
        }
    }

}