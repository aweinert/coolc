package net.alexweinert.coolc.representations.cool.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.representations.cool.Utilities;
import net.alexweinert.coolc.representations.cool.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.ClassTable;
import net.alexweinert.coolc.representations.cool.symboltables.FeatureTable;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.TreeConstants;

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
    public If(String filename, int lineNumber, Expression a1, Expression a2, Expression a3) {
        super(filename, lineNumber);
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
    protected IdSymbol inferType(ClassNode enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        IdSymbol conditionType = this.pred.typecheck(enclosingClass, classTable, featureTable);
        IdSymbol thenBranchType = this.then_exp.typecheck(enclosingClass, classTable, featureTable);
        IdSymbol elseBranchType = this.else_exp.typecheck(enclosingClass, classTable, featureTable);

        if (!classTable.conformsTo(enclosingClass.getIdentifier(), conditionType, TreeConstants.Bool)) {
            String errorString = "Predicate of 'if' does not have type Bool.";
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
        }

        return classTable.getLeastUpperBound(thenBranchType, elseBranchType);
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitIfPreorder(this);
        this.pred.acceptVisitor(visitor);
        visitor.visitIfPreorderOne(this);
        this.then_exp.acceptVisitor(visitor);
        visitor.visitIfPreorderTwo(this);
        this.else_exp.acceptVisitor(visitor);
        visitor.visitIfPostorder(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((else_exp == null) ? 0 : else_exp.hashCode());
        result = prime * result + ((pred == null) ? 0 : pred.hashCode());
        result = prime * result + ((then_exp == null) ? 0 : then_exp.hashCode());
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
        If other = (If) obj;
        if (else_exp == null) {
            if (other.else_exp != null) {
                return false;
            }
        } else if (!else_exp.equals(other.else_exp)) {
            return false;
        }
        if (pred == null) {
            if (other.pred != null) {
                return false;
            }
        } else if (!pred.equals(other.pred)) {
            return false;
        }
        if (then_exp == null) {
            if (other.then_exp != null) {
                return false;
            }
        } else if (!then_exp.equals(other.then_exp)) {
            return false;
        }
        return true;
    }

    public Expression getCondition() {
        return this.pred;
    }

}