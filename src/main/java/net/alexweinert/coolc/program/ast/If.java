package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'cond'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class If extends Expression {
    final protected Expression pred;
    final protected Expression then_exp;
    final protected Expression else_exp;

    /**
     * Creates "cond" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for pred
     * @param a1
     *            initial value for then_exp
     * @param a2
     *            initial value for else_exp
     */
    public If(int lineNumber, Expression a1, Expression a2, Expression a3) {
        super(lineNumber);
        pred = a1;
        then_exp = a2;
        else_exp = a3;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "cond\n");
        pred.dump(out, n + 2);
        then_exp.dump(out, n + 2);
        else_exp.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_cond");
        pred.dump_with_types(out, n + 2);
        then_exp.dump_with_types(out, n + 2);
        else_exp.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    protected AbstractSymbol inferType(Class enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        AbstractSymbol conditionType = this.pred.typecheck(enclosingClass, classTable, featureTable);
        AbstractSymbol thenBranchType = this.then_exp.typecheck(enclosingClass, classTable, featureTable);
        AbstractSymbol elseBranchType = this.else_exp.typecheck(enclosingClass, classTable, featureTable);

        if (!classTable.conformsTo(enclosingClass.getName(), conditionType, TreeConstants.Bool)) {
            String errorString = "Predicate of 'if' does not have type Bool.";
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
        }

        return classTable.getLeastUpperBound(thenBranchType, elseBranchType);
    }

}