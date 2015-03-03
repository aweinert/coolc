package net.alexweinert.coolc.representations.cool.ast;

// -*- mode: java -*-
//
// file: cool-tree.m4
//
// This file defines the AST
//
// ////////////////////////////////////////////////////////

import java.io.PrintStream;
import java.util.Map;

import net.alexweinert.coolc.representations.cool.Utilities;
import net.alexweinert.coolc.representations.cool.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.ClassTable;
import net.alexweinert.coolc.representations.cool.symboltables.FeatureTable;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.TreeConstants;

/**
 * Defines AST constructor 'object'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class ObjectReference extends Expression {
    final protected IdSymbol name;

    /**
     * Creates "object" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     */
    public ObjectReference(String filename, int lineNumber, IdSymbol a1) {
        super(filename, lineNumber);
        name = a1;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "object\n");
        dump_IdSymbol(out, n + 2, name);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_object");
        dump_IdSymbol(out, n + 2, name);
        dump_type(out, n);
    }

    @Override
    protected IdSymbol inferType(ClassNode enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        Map<IdSymbol, IdSymbol> attributeTypes = featureTable.getAttributeTypes(enclosingClass
                .getIdentifier());
        if (!attributeTypes.containsKey(this.name)) {
            String errorString = String.format("Undeclared identifier %s.", this.name);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            return TreeConstants.Object_;
        }
        return attributeTypes.get(this.name);
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitObjectReference(this);
    }

    public IdSymbol getVariableIdentifier() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
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
        ObjectReference other = (ObjectReference) obj;
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
