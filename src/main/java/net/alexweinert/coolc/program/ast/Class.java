package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;

/**
 * Defines AST constructor 'class_c'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Class extends TreeNode {
    final protected AbstractSymbol name;
    final protected AbstractSymbol parent;
    final protected Features features;
    final protected AbstractSymbol filename;

    /**
     * Creates "class_c" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for parent
     * @param a2
     *            initial value for features
     * @param a3
     *            initial value for filename
     */
    public Class(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Features a3, AbstractSymbol a4) {
        super(lineNumber);
        name = a1;
        parent = a2;
        features = a3;
        filename = a4;
    }

    public void typecheck(ClassTable classTable, FeatureTable featureTable) {
        // Typecheck each feature on its own
        for (final Feature currentFeature : this.features) {
            currentFeature.typecheck(this, classTable, featureTable);
        }
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "class_c\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        features.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, filename);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_class");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, filename.getString());
        out.println("\"\n" + Utilities.pad(n + 2) + "(");
        for (final Feature currentFeature : this.features) {
            currentFeature.dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitClassPreorder(this);
        this.features.acceptVisitor(visitor);
        visitor.visitClassPostorder(this);

    }

    public AbstractSymbol getIdentifier() {
        return name;
    }

    public AbstractSymbol getParent() {
        return parent;
    }

    public AbstractSymbol getFilename() {
        return filename;
    }

    public Features getFeatures() {
        return features;
    }

}