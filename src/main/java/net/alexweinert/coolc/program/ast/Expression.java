package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;

/** Defines simple phylum Expression */
public abstract class Expression extends TreeNode {
    protected Expression(int lineNumber) {
        super(lineNumber);
    }

    private AbstractSymbol type = null;

    public AbstractSymbol get_type() {
        return type;
    }

    public Expression set_type(AbstractSymbol s) {
        type = s;
        return this;
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public void dump_type(PrintStream out, int n) {
        if (type != null) {
            out.println(Utilities.pad(n) + ": " + type.getString());
        } else {
            out.println(Utilities.pad(n) + ": _no_type");
        }
    }

    public AbstractSymbol typecheck(Class_ enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        AbstractSymbol checkedType = this.inferType(enclosingClass, classTable, featureTable);
        this.set_type(checkedType);
        return checkedType;
    }

    protected abstract AbstractSymbol inferType(Class_ enclosingClass, ClassTable classTable, FeatureTable featureTable);

}