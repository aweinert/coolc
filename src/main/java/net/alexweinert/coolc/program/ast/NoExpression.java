package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'no_expr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class NoExpression extends Expression {
    /**
     * Creates "no_expr" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     */
    public NoExpression(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "no_expr\n");
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_no_expr");
        dump_type(out, n);
    }

    @Override
    protected IdSymbol inferType(Class enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        return TreeConstants.No_type;
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitNoExpression(this);
    }

}