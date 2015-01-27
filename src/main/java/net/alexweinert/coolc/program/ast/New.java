package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'new_'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class New extends Expression {
    final protected AbstractSymbol type_name;

    /**
     * Creates "new_" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for type_name
     */
    public New(String filename, int lineNumber, AbstractSymbol a1) {
        super(filename, lineNumber);
        type_name = a1;
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
    protected AbstractSymbol inferType(Class enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        if (classTable.classExists(this.type_name)) {
            return this.type_name;
        } else {
            String errorString = String.format("'new' used with undefined class %s.", this.type_name);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            return TreeConstants.Object_;
        }
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitNew(this);
    }

    public AbstractSymbol getTypeIdentifier() {
        return type_name;
    }

}