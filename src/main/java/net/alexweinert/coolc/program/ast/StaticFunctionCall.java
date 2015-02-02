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
    public StaticFunctionCall(String filename, int lineNumber, Expression a1, AbstractSymbol a2, AbstractSymbol a3,
            Expressions a4) {
        super(filename, lineNumber);
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
        if (!classTable.conformsTo(enclosingClass.getIdentifier(), expressionType, this.type_name)) {
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

            if (!classTable.conformsTo(enclosingClass.getIdentifier(), actualType, formalType)) {
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

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitStaticFunctionCallPreorder(this);
        this.expr.acceptVisitor(visitor);
        visitor.visitStaticFunctionCallInorder(this);
        this.actual.acceptVisitor(visitor);
        visitor.visitStaticFunctionCallPostorder(this);
    }

    public AbstractSymbol getStaticType() {
        return type_name;
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
        result = prime * result + ((type_name == null) ? 0 : type_name.hashCode());
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
        StaticFunctionCall other = (StaticFunctionCall) obj;
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
        if (type_name == null) {
            if (other.type_name != null) {
                return false;
            }
        } else if (!type_name.equals(other.type_name)) {
            return false;
        }
        return true;
    }

}