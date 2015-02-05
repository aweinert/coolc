package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;

/** Defines simple phylum Feature */
public abstract class Feature extends TreeNode {
    final protected IdSymbol name;

    protected Feature(String filename, int lineNumber, IdSymbol name) {
        super(filename, lineNumber);
        this.name = name;
    }

    public IdSymbol getName() {
        return this.name;
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public abstract void typecheck(Class enclosingClass, ClassTable classTable, FeatureTable featureTable);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Feature other = (Feature) obj;
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