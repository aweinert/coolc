package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'sub'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Subtraction extends Expression {
    final protected Expression e1;
    final protected Expression e2;

    /**
     * Creates "sub" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     * @param a1
     *            initial value for e2
     */
    public Subtraction(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "sub\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_sub");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    protected AbstractSymbol inferType(Class enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        AbstractSymbol leftHandType = this.e1.typecheck(enclosingClass, classTable, featureTable);
        AbstractSymbol rightHandType = this.e2.typecheck(enclosingClass, classTable, featureTable);

        if (!(classTable.conformsTo(enclosingClass.getName(), leftHandType, TreeConstants.Int) && classTable
                .conformsTo(enclosingClass.getName(), rightHandType, TreeConstants.Int))) {
            String errorString = String.format("non-Int arguments: %s - %s", leftHandType, rightHandType);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
        }

        return TreeConstants.Int;
    }

}