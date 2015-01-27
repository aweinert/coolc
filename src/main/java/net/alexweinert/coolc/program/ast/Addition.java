package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'plus'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Addition extends Expression {
    final private Expression e1;
    final private Expression e2;

    /**
     * Creates "plus" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a1
     *            initial value for e1
     * @param a2
     *            initial value for e2
     */
    public Addition(String filename, int lineNumber, Expression a1, Expression a2) {
        super(filename, lineNumber);
        this.e1 = a1;
        this.e2 = a2;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "plus\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_plus");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    protected AbstractSymbol inferType(Class enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        AbstractSymbol leftHandType = this.e1.typecheck(enclosingClass, classTable, featureTable);
        AbstractSymbol rightHandType = this.e2.typecheck(enclosingClass, classTable, featureTable);

        if (!(classTable.conformsTo(enclosingClass.getIdentifier(), leftHandType, TreeConstants.Int) && classTable
                .conformsTo(enclosingClass.getIdentifier(), rightHandType, TreeConstants.Int))) {
            String errorString = String.format("non-Int arguments: %s + %s", leftHandType, rightHandType);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
        }

        return TreeConstants.Int;
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitAdditionPreorder(this);
        this.e1.acceptVisitor(visitor);
        visitor.visitAdditionInorder(this);
        this.e2.acceptVisitor(visitor);
        visitor.visitAdditionPostorder(this);
    }

}