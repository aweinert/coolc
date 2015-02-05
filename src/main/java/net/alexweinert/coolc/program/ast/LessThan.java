package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'lt'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class LessThan extends Expression {
    final protected Expression e1;
    final protected Expression e2;

    /**
     * Creates "lt" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     * @param a1
     *            initial value for e2
     */
    public LessThan(String filename, int lineNumber, Expression a1, Expression a2) {
        super(filename, lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "lt\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_lt");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    protected IdSymbol inferType(Class enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        IdSymbol leftHandType = this.e1.typecheck(enclosingClass, classTable, featureTable);
        IdSymbol rightHandType = this.e2.typecheck(enclosingClass, classTable, featureTable);

        if (!(classTable.conformsTo(enclosingClass.getIdentifier(), leftHandType, TreeConstants.Int) && classTable
                .conformsTo(enclosingClass.getIdentifier(), rightHandType, TreeConstants.Int))) {
            String errorString = String.format("non-Int arguments: %s < %s", leftHandType, rightHandType);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
        }

        return TreeConstants.Bool;
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitLessThanPreorder(this);
        this.e1.acceptVisitor(visitor);
        visitor.visitLessThanInorder(this);
        this.e2.acceptVisitor(visitor);
        visitor.visitLessThanPostorder(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
        result = prime * result + ((e2 == null) ? 0 : e2.hashCode());
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
        LessThan other = (LessThan) obj;
        if (e1 == null) {
            if (other.e1 != null) {
                return false;
            }
        } else if (!e1.equals(other.e1)) {
            return false;
        }
        if (e2 == null) {
            if (other.e2 != null) {
                return false;
            }
        } else if (!e2.equals(other.e2)) {
            return false;
        }
        return true;
    }

}