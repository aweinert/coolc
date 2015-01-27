package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'assign'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Assign extends Expression {
    final protected AbstractSymbol name;

    final protected Expression expr;

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
    public Assign(String filename, int lineNumber, AbstractSymbol a1, Expression a2) {
        super(filename, lineNumber);
        this.name = a1;
        this.expr = a2;
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
    protected AbstractSymbol inferType(Class enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        AbstractSymbol leftHandType = featureTable.getAttributeTypes(enclosingClass.getIdentifier()).get(this.name);
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

        if (!classTable.conformsTo(enclosingClass.getIdentifier(), rightHandType, leftHandType)) {
            String errorString = String.format(
                    "Type %s of assigned expression does not conform to declared type %s of identifier %s.",
                    rightHandType, leftHandType, this.name);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            // Safe approach: Just return Object in error case
            return TreeConstants.Object_;
        }

        return rightHandType;
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitAssignPreorder(this);
        this.expr.acceptVisitor(visitor);
        visitor.visitAssignPostorder(this);
    }

    public AbstractSymbol getVariableIdentifier() {
        return name;
    }

}