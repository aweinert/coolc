package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'comp'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class BooleanNegation extends Expression {
    final protected Expression e1;

    /**
     * Creates "comp" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     */
    public BooleanNegation(int lineNumber, Expression a1) {
        super(lineNumber);
        this.e1 = a1;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "comp\n");
        e1.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_comp");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    protected AbstractSymbol inferType(Class enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        AbstractSymbol operandType = this.e1.typecheck(enclosingClass, classTable, featureTable);

        if (!classTable.conformsTo(enclosingClass.getName(), operandType, TreeConstants.Bool)) {
            String errorString = String.format("Argument of 'not' has type %s instead of Bool.", operandType);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
        }

        return TreeConstants.Bool;
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitBooleanNegationPreorder(this);
        this.e1.acceptVisitor(visitor);
        visitor.visitBooleanNegationPostorder(this);

    }

}