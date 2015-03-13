package net.alexweinert.coolc.representations.cool.program.parsed;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.util.TreeNode;

/** Defines simple phylum Feature */
public abstract class ParsedFeature extends TreeNode {
    final protected IdSymbol name;

    protected ParsedFeature(String filename, int lineNumber, IdSymbol name) {
        super(filename, lineNumber);
        this.name = name;
    }

    public IdSymbol getName() {
        return this.name;
    }

    public abstract void acceptVisitor(ParsedProgramVisitor visitor);

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
        ParsedFeature other = (ParsedFeature) obj;
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