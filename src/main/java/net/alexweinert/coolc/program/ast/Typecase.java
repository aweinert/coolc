package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;
import java.util.Enumeration;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
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
    public Typecase(String filename, int lineNumber, Expression a1, Cases a2) {
        super(filename, lineNumber);
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
    protected IdSymbol inferType(Class enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        // We do not need the static type of the expression anywhere, but we need to typecheck it anyways
        IdSymbol expressionType = this.expr.typecheck(enclosingClass, classTable, featureTable);

        IdSymbol leastUpperBound = null;
        for (final Case currentBranch : this.cases) {
            // Check that we do not try to bind self
            if (currentBranch.name.equals(TreeConstants.self)) {
                String errorString = "'self' bound in 'case'.";
                classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            }

            FeatureTable extendedTable = featureTable.copyAndExtend(enclosingClass.getIdentifier(), currentBranch.name,
                    currentBranch.type_decl);
            IdSymbol caseType = currentBranch.expr.typecheck(enclosingClass, classTable, extendedTable);
            if (leastUpperBound == null) {
                leastUpperBound = caseType;
            } else {
                leastUpperBound = classTable.getLeastUpperBound(leastUpperBound, caseType);
            }
        }

        return leastUpperBound;
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitTypecasePreorder(this);
        this.expr.acceptVisitor(visitor);
        visitor.visitTypecaseInorder(this);
        this.cases.acceptVisitor(visitor);
        visitor.visitTypecasePostorder(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((cases == null) ? 0 : cases.hashCode());
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Typecase other = (Typecase) obj;
        if (cases == null) {
            if (other.cases != null) {
                return false;
            }
        } else if (!cases.equals(other.cases)) {
            return false;
        }
        if (expr == null) {
            if (other.expr != null) {
                return false;
            }
        } else if (!expr.equals(other.expr)) {
            return false;
        }
        return true;
    }

}