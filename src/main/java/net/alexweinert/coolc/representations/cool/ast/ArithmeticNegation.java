package net.alexweinert.coolc.representations.cool.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.representations.cool.Utilities;
import net.alexweinert.coolc.representations.cool.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.ClassTable;
import net.alexweinert.coolc.representations.cool.symboltables.FeatureTable;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.TreeConstants;

/**
 * Defines AST constructor 'neg'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class ArithmeticNegation extends Expression {
    final protected Expression e1;

    /**
     * Creates "neg" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     */
    public ArithmeticNegation(String filename, int lineNumber, Expression a1) {
        super(filename, lineNumber);
        e1 = a1;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "neg\n");
        e1.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_neg");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    protected IdSymbol inferType(ClassNode enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        IdSymbol operandType = this.e1.typecheck(enclosingClass, classTable, featureTable);

        if (!classTable.conformsTo(enclosingClass.getIdentifier(), operandType, TreeConstants.Int)) {
            String errorString = String.format("Argument of '~' has type %s instead of Int.", operandType);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
        }

        return TreeConstants.Int;
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitArithmeticNegationPreorder(this);
        this.e1.acceptVisitor(visitor);
        visitor.visitArithmeticNegationPostOrder(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
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
        ArithmeticNegation other = (ArithmeticNegation) obj;
        if (e1 == null) {
            if (other.e1 != null) {
                return false;
            }
        } else if (!e1.equals(other.e1)) {
            return false;
        }
        return true;
    }

}