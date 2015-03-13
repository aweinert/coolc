package net.alexweinert.coolc.representations.cool.program.parsed;

import java.util.Iterator;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;
import net.alexweinert.coolc.representations.cool.symboltables.IntTable;
import net.alexweinert.coolc.representations.cool.symboltables.StringTable;
import net.alexweinert.coolc.representations.cool.symboltables.SymbolTables;
import net.alexweinert.coolc.representations.cool.util.TreeNode;

/**
 * The complete program. Root node of a well-formed AST
 */
public class ParsedProgram extends TreeNode {
    private final SymbolTables symbolTables;
    protected final Classes classes;

    /**
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param classes
     *            initial value for classes
     */
    public ParsedProgram(String filename, int lineNumber, Classes classes, SymbolTables symbolTables) {
        super(filename, lineNumber);
        this.classes = classes;
        this.symbolTables = symbolTables;
    }

    /**
     * @param identifier
     *            Some class identifier
     * @return The node of the first defined class with the given name, if there exists at least one. Null if none
     *         exists.
     */
    public ClassNode getClass(IdSymbol identifier) {
        for (ClassNode classNode : this.classes) {
            if (classNode.getIdentifier().equals(identifier)) {
                return classNode;
            }
        }
        return null;
    }

    public Classes getClasses() {
        return this.classes;
    }

    public SymbolTables getSymbolTables() {
        return this.symbolTables;
    }

    public IdTable getIdTable() {
        return symbolTables.getIdTable();
    }

    public StringTable getStringTable() {
        return this.symbolTables.getStringTable();
    }

    public IntTable getIntTable() {
        return this.symbolTables.getIntTable();
    }

    public void acceptVisitor(ParsedProgramVisitor visitor) {
        visitor.visitProgramPreorder(this);
        this.classes.acceptVisitor(visitor);
        visitor.visitProgramPostorder(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classes == null) ? 0 : classes.hashCode());
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
        ParsedProgram other = (ParsedProgram) obj;
        if (classes == null) {
            if (other.classes != null) {
                return false;
            }
        } else if (!classes.equals(other.classes)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Iterator<ClassNode> it = this.classes.iterator();
        while (it.hasNext()) {
            builder.append(it.next().toString());
            if (it.hasNext()) {
                builder.append("\n\n");
            }
        }
        return builder.toString();
    }
}