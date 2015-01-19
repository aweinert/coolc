package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'attr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class attr extends Feature {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression init;

    /**
     * Creates "attr" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for type_decl
     * @param a2
     *            initial value for init
     */
    public attr(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        init = a3;
    }

    public TreeNode copy() {
        return new attr(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl), (Expression) init.copy());
    }

    @Override
    public void typecheck(Class_ enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        if (this.init instanceof no_expr) {
            // If we have no initializer, we simply believe whatever the declaration tells us
            return;
        } else {
            FeatureTable extendedFeatureTable = featureTable.copyAndExtend(enclosingClass.getName(),
                    TreeConstants.self, TreeConstants.SELF_TYPE);
            AbstractSymbol initializerType = this.init.typecheck(enclosingClass, classTable, extendedFeatureTable);
            // Could also use the featureTable at this point, but taking the declared type directly saves us one
            // indirection
            AbstractSymbol declaredType = this.type_decl;
            if (!classTable.conformsTo(enclosingClass.getName(), initializerType, declaredType)) {
                String errorString = String.format(
                        "Inferred type %s of initialization of attribute %s does not conform to declared type %s.",
                        initializerType, this.name, declaredType);
                classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            }
        }
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "attr\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_attr");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump_with_types(out, n + 2);
    }

}