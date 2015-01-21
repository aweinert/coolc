package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;
import java.util.Enumeration;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'typcase'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Typecase extends Expression {
    final protected Expression expr;
    final protected Cases cases;

    /**
     * Creates "typcase" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for expr
     * @param a1
     *            initial value for cases
     */
    public Typecase(int lineNumber, Expression a1, Cases a2) {
        super(lineNumber);
        expr = a1;
        cases = a2;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "typcase\n");
        expr.dump(out, n + 2);
        cases.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_typcase");
        expr.dump_with_types(out, n + 2);
        for (final Case currentCase : cases) {
            currentCase.dump_with_types(out, n + 2);
        }
        dump_type(out, n);
    }

    @Override
    protected AbstractSymbol inferType(Class enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        // We do not need the static type of the expression anywhere, but we need to typecheck it anyways
        AbstractSymbol expressionType = this.expr.typecheck(enclosingClass, classTable, featureTable);

        AbstractSymbol leastUpperBound = null;
        for (final Case currentBranch : this.cases) {
            // Check that we do not try to bind self
            if (currentBranch.name.equals(TreeConstants.self)) {
                String errorString = "'self' bound in 'case'.";
                classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            }

            FeatureTable extendedTable = featureTable.copyAndExtend(enclosingClass.getName(), currentBranch.name,
                    currentBranch.type_decl);
            AbstractSymbol caseType = currentBranch.expr.typecheck(enclosingClass, classTable, extendedTable);
            if (leastUpperBound == null) {
                leastUpperBound = caseType;
            } else {
                leastUpperBound = classTable.getLeastUpperBound(leastUpperBound, caseType);
            }
        }

        return leastUpperBound;
    }

}