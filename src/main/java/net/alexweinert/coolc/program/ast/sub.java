package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

/**
 * Defines AST constructor 'sub'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class sub extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "sub" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     * @param a1
     *            initial value for e2
     */
    public sub(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new sub(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "sub\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_sub");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    protected AbstractSymbol inferType(Class_ enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        AbstractSymbol leftHandType = this.e1.typecheck(enclosingClass, classTable, featureTable);
        AbstractSymbol rightHandType = this.e2.typecheck(enclosingClass, classTable, featureTable);

        if (!(classTable.conformsTo(enclosingClass.getName(), leftHandType, TreeConstants.Int) && classTable
                .conformsTo(enclosingClass.getName(), rightHandType, TreeConstants.Int))) {
            String errorString = String.format("non-Int arguments: %s - %s", leftHandType, rightHandType);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
        }

        return TreeConstants.Int;
    }

}