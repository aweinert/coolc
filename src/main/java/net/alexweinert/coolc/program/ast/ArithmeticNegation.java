package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

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
    protected AbstractSymbol inferType(Class enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        AbstractSymbol operandType = this.e1.typecheck(enclosingClass, classTable, featureTable);

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

}