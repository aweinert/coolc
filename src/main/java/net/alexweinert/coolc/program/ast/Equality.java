package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'eq'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Equality extends Expression {
    final protected Expression e1;
    final protected Expression e2;

    /**
     * Creates "eq" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     * @param a1
     *            initial value for e2
     */
    public Equality(String filename, int lineNumber, Expression a1, Expression a2) {
        super(filename, lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "eq\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_eq");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    protected IdSymbol inferType(ClassNode enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        IdSymbol leftHandType = this.e1.typecheck(enclosingClass, classTable, featureTable);
        IdSymbol rightHandType = this.e2.typecheck(enclosingClass, classTable, featureTable);

        if ((isBasicType(leftHandType) || isBasicType(rightHandType)) && (!leftHandType.equals(rightHandType))) {
            String errorString = String.format("Illegal comparison with a basic type.");
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
        }

        return TreeConstants.Bool;
    }

    private boolean isBasicType(IdSymbol candidate) {
        return candidate.equals(TreeConstants.Int) || candidate.equals(TreeConstants.Bool)
                || candidate.equals(TreeConstants.Str);
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitEqualityPreorder(this);
        this.e1.acceptVisitor(visitor);
        visitor.visitEqualityInorder(this);
        this.e2.acceptVisitor(visitor);
        visitor.visitEqualityPostorder(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
        result = prime * result + ((e2 == null) ? 0 : e2.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Equality other = (Equality) obj;
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