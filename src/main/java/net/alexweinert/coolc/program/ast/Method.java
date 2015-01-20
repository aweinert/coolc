package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;
import java.util.Enumeration;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'method'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Method extends Feature {
    protected AbstractSymbol name;
    protected Formals formals;
    protected AbstractSymbol return_type;
    protected Expression expr;

    /**
     * Creates "method" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for formals
     * @param a2
     *            initial value for return_type
     * @param a3
     *            initial value for expr
     */
    public Method(int lineNumber, AbstractSymbol a1, Formals a2, AbstractSymbol a3, Expression a4) {
        super(lineNumber);
        name = a1;
        formals = a2;
        return_type = a3;
        expr = a4;
    }

    public TreeNode copy() {
        return new Method(lineNumber, copy_AbstractSymbol(name), (Formals) formals.copy(),
                copy_AbstractSymbol(return_type), (Expression) expr.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "method\n");
        dump_AbstractSymbol(out, n + 2, name);
        formals.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, return_type);
        expr.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_method");
        dump_AbstractSymbol(out, n + 2, name);
        for (Enumeration e = formals.getElements(); e.hasMoreElements();) {
            ((Formal) e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_AbstractSymbol(out, n + 2, return_type);
        expr.dump_with_types(out, n + 2);
    }

    @Override
    public void typecheck(Class_ enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        FeatureTable extendedFeatureTable = featureTable.copyAndExtend(enclosingClass.getName(), TreeConstants.self,
                TreeConstants.SELF_TYPE);

        for (int formalIndex = 0; formalIndex < this.formals.getLength(); ++formalIndex) {
            FormalConstructor currentFormal = (FormalConstructor) this.formals.getNth(formalIndex);
            extendedFeatureTable = extendedFeatureTable.copyAndExtend(enclosingClass.getName(), currentFormal.name,
                    currentFormal.type_decl);
        }

        AbstractSymbol bodyType = this.expr.typecheck(enclosingClass, classTable, extendedFeatureTable);

        if (!classTable.conformsTo(enclosingClass.getName(), bodyType, this.return_type)) {
            String errorString = String.format(
                    "Inferred return type %s of method %s does not conform to declared return type %s.", bodyType,
                    this.name, this.return_type);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
        }
    }
}