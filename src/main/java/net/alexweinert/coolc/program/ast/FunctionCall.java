package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;
import java.util.Enumeration;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class FunctionCall extends Expression {
    final protected Expression expr;
    final protected AbstractSymbol name;
    final protected Expressions actual;

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
    public FunctionCall(String filename, int lineNumber, Expression a1, AbstractSymbol a2, Expressions a3) {
        super(filename, lineNumber);
        expr = a1;
        name = a2;
        actual = a3;
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
        for (final Expression parameter : this.actual) {
            parameter.dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
        dump_type(out, n);
    }

    @Override
    protected AbstractSymbol inferType(Class enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        AbstractSymbol expressionType = this.expr.typecheck(enclosingClass, classTable, featureTable);
        final AbstractSymbol dispatchTargetClass;
        if (expressionType.equals(TreeConstants.SELF_TYPE)) {
            dispatchTargetClass = enclosingClass.getIdentifier();
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

            if (!classTable.conformsTo(enclosingClass.getIdentifier(), actualType, formalType)) {
                Method targetMethodDef = featureTable.findMethodDefinition(classTable.getClass(dispatchTargetClass),
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

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitFunctionCallPreorder(this);
        this.expr.acceptVisitor(visitor);
        visitor.visitFunctionCallInorder(this);
        this.actual.acceptVisitor(visitor);
        visitor.visitFunctionCallPostorder(this);
    }

    public AbstractSymbol getFunctionIdentifier() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((actual == null) ? 0 : actual.hashCode());
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FunctionCall other = (FunctionCall) obj;
        if (actual == null) {
            if (other.actual != null) {
                return false;
            }
        } else if (!actual.equals(other.actual)) {
            return false;
        }
        if (expr == null) {
            if (other.expr != null) {
                return false;
            }
        } else if (!expr.equals(other.expr)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}