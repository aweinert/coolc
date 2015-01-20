package net.alexweinert.coolc.program.ast;

// -*- mode: java -*-
//
// file: cool-tree.m4
//
// This file defines the AST
//
// ////////////////////////////////////////////////////////

import java.io.PrintStream;
import java.util.Map;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'object'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class ObjectReference extends Expression {
    protected AbstractSymbol name;

    /**
     * Creates "object" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     */
    public ObjectReference(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        name = a1;
    }

    public TreeNode copy() {
        return new ObjectReference(lineNumber, copy_AbstractSymbol(name));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "object\n");
        dump_AbstractSymbol(out, n + 2, name);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_object");
        dump_AbstractSymbol(out, n + 2, name);
        dump_type(out, n);
    }

    @Override
    protected AbstractSymbol inferType(Class_ enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        Map<AbstractSymbol, AbstractSymbol> attributeTypes = featureTable.getAttributeTypes(enclosingClass.getName());
        if (!attributeTypes.containsKey(this.name)) {
            String errorString = String.format("Undeclared identifier %s.", this.name);
            classTable.semantError(enclosingClass.getFilename(), this).println(errorString);
            return TreeConstants.Object_;
        }
        return attributeTypes.get(this.name);
    }

}
