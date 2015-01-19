package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;
import java.util.Enumeration;

import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol name;
    protected Expressions actual;

    /**
     * Creates "dispatch" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for expr
     * @param a1
     *            initial value for name
     * @param a2
     *            initial value for actual
     */
    public dispatch(int lineNumber, Expression a1, AbstractSymbol a2, Expressions a3) {
        super(lineNumber);
        expr = a1;
        name = a2;
        actual = a3;
    }

    public TreeNode copy() {
        return new dispatch(lineNumber, (Expression) expr.copy(), copy_AbstractSymbol(name),
                (Expressions) actual.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "dispatch\n");
        expr.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, name);
        actual.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_dispatch");
        expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
            ((Expression) e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
        dump_type(out, n);
    }

    @Override
    protected AbstractSymbol inferType(Class_ enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        AbstractSymbol expressionType = this.expr.typecheck(enclosingClass, classTable, featureTable);
        final AbstractSymbol dispatchTargetClass;
        if (expressionType.equals(TreeConstants.SELF_TYPE)) {
            dispatchTargetClass = enclosingClass.getName();
        } else {
            dispatchTargetClass = expressionType;
        }

        if (!featureTable.getMethodSignatures(dispatchTargetClass).containsKey(this.name)) {
            String errorString = String.format("Dispatch to undefined method %s.", this.name);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            return TreeConstants.Object_;
        }

        FeatureTable.MethodSignature targetSignature = featureTable.getMethodSignatures(dispatchTargetClass).get(
                this.name);

        // Check that the number of arguments is the same in the definition and the call
        if (this.actual.getLength() != targetSignature.getArgumentTypes().size()) {
            String errorString = String.format("Method %s called with wrong number of arguments.", this.name);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            return targetSignature.getReturnType();
        }

        // Check that all the actual parameters conform to the formal parameters
        for (int actualIndex = 0; actualIndex < this.actual.getLength(); ++actualIndex) {
            AbstractSymbol actualType = ((Expression) this.actual.getNth(actualIndex)).typecheck(enclosingClass,
                    classTable, featureTable);
            AbstractSymbol formalType = targetSignature.getArgumentTypes().get(actualIndex);

            if (!classTable.conformsTo(enclosingClass.getName(), actualType, formalType)) {
                method targetMethodDef = featureTable.findMethodDefinition(classTable.getClass(dispatchTargetClass),
                        this.name);
                AbstractSymbol formalName = ((formalc) targetMethodDef.formals.getNth(actualIndex)).name;
                String errorString = String.format(
                        "In call of method %s, type %s of parameter %s does not conform to declared type %s.",
                        this.name, actualType, formalName, formalType);
                classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            }
        }

        if (targetSignature.getReturnType().equals(TreeConstants.SELF_TYPE)) {
            return expressionType;
        } else {
            return targetSignature.getReturnType();
        }

    }

}