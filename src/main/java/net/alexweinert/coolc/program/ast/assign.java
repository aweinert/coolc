package net.alexweinert.coolc.program.ast;

/**
 * Defines AST constructor 'assign'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class assign extends Expression {
    protected AbstractSymbol name;
    protected Expression expr;

    /**
     * Creates "assign" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for expr
     */
    public assign(int lineNumber, AbstractSymbol a1, Expression a2) {
        super(lineNumber);
        name = a1;
        expr = a2;
    }

    public TreeNode copy() {
        return new assign(lineNumber, copy_AbstractSymbol(name), (Expression) expr.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "assign\n");
        dump_AbstractSymbol(out, n + 2, name);
        expr.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_assign");
        dump_AbstractSymbol(out, n + 2, name);
        expr.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    protected AbstractSymbol inferType(Class_ enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        AbstractSymbol leftHandType = featureTable.getAttributeTypes(enclosingClass.getName()).get(this.name);
        AbstractSymbol rightHandType = this.expr.typecheck(enclosingClass, classTable, featureTable);

        // Check that the left-hand-side of the assignment is not self
        if (this.name.equals(TreeConstants.self)) {
            String errorString = "Cannot assign to 'self'";
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            return rightHandType;
        }

        // Check that the left hand variable exists at all
        if (leftHandType == null) {
            String errorString = String.format("Assignment to undeclared variable %s", this.name);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            return rightHandType;
        }

        if (!classTable.conformsTo(enclosingClass.getName(), rightHandType, leftHandType)) {
            String errorString = String.format(
                    "Type %s of assigned expression does not conform to declared type %s of identifier %s.",
                    rightHandType, leftHandType, this.name);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            // Safe approach: Just return Object in error case
            return TreeConstants.Object_;
        }

        return rightHandType;
    }

}