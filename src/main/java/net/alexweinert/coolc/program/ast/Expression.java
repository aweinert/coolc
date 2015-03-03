package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;

/** Defines simple phylum Expression */
public abstract class Expression extends TreeNode {
    private IdSymbol type = null;

    protected Expression(String filename, int lineNumber) {
        super(filename, lineNumber);
    }
    
    public IdSymbol getType() {
        return this.type;
    }

    public void setType(IdSymbol type) {
        this.type = type;
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public void dump_type(PrintStream out, int n) {
        if (type != null) {
            out.println(Utilities.pad(n) + ": " + type.getString());
        } else {
            out.println(Utilities.pad(n) + ": _no_type");
        }
    }

    public IdSymbol typecheck(ClassNode enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        IdSymbol checkedType = this.inferType(enclosingClass, classTable, featureTable);
        return checkedType;
    }

    protected abstract IdSymbol inferType(ClassNode enclosingClass, ClassTable classTable, FeatureTable featureTable);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Expression other = (Expression) obj;
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

}