package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;
import java.util.Enumeration;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'static_dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class StaticFunctionCall extends Expression {
    final protected Expression expr;
    final protected AbstractSymbol type_name;
    final protected AbstractSymbol name;
    final protected Expressions actual;

    /**
     * Creates "static_dispatch" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for expr
     * @param a1
     *            initial value for type_name
     * @param a2
     *            initial value for name
     * @param a3
     *            initial value for actual
     */
    public StaticFunctionCall(int lineNumber, Expression a1, AbstractSymbol a2, AbstractSymbol a3, Expressions a4) {
        super(lineNumber);
        expr = a1;
        type_name = a2;
        name = a3;
        actual = a4;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "static_dispatch\n");
        expr.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_AbstractSymbol(out, n + 2, name);
        actual.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_static_dispatch");
        expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (final Expression actual : this.actual) {
            actual.dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
        dump_type(out, n);
    }

    @Override
    protected AbstractSymbol inferType(Class enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        AbstractSymbol expressionType = this.expr.typecheck(enclosingClass, classTable, featureTable);
        if (!classTable.conformsTo(enclosingClass.getName(), expressionType, this.type_name)) {
            String errorString = String.format(
                    "Expression type %s does not conform to declared static dispatch type %s.", expressionType,
                    this.type_name);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
        }

        if (!featureTable.getMethodSignatures(this.type_name).containsKey(this.name)) {
            String errorString = String.format("Dispatch to undefined method %s.", this.name);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            return TreeConstants.Object_;
        }

        // Check that the number of arguments is the same in the definition and the call
        FeatureTable.MethodSignature targetSignature = featureTable.getMethodSignatures(this.type_name).get(this.name);
        if (this.actual.size() != targetSignature.getArgumentTypes().size()) {
            String errorString = String.format("Method %s called with wrong number of arguments.", this.name);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            return targetSignature.getReturnType();
        }

        // Check that all the actual parameters conform to the formal parameters
        for (int actualIndex = 0; actualIndex < this.actual.size(); ++actualIndex) {
            AbstractSymbol actualType = ((Expression) this.actual.get(actualIndex)).typecheck(enclosingClass,
                    classTable, featureTable);
            AbstractSymbol formalType = targetSignature.getArgumentTypes().get(actualIndex);

            if (!classTable.conformsTo(enclosingClass.getName(), actualType, formalType)) {
                Method targetMethodDef = featureTable.findMethodDefinition(classTable.getClass(this.type_name),
                        this.name);
                AbstractSymbol formalName = ((Formal) targetMethodDef.formals.get(actualIndex)).name;
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