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
     */
    public Class(String filename, int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Features a3) {
        super(filename, lineNumber);
        name = a1;
        parent = a2;
        features = a3;
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
        out.print(Utilities.pad(n + 2) + this.getFilename());
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_class");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, this.getFilename());
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

    public Features getFeatures() {
        return features;
    }

    public Attribute getAttribute(AbstractSymbol identifier) {
        for (Feature feature : this.features) {
            if (feature.getName().equals(identifier) && feature instanceof Attribute) {
                return (Attribute) feature;
            }
        }
        return null;
    }

    public Method getMethod(AbstractSymbol identifier) {
        for (Feature feature : this.features) {
            if (feature.getName().equals(identifier) && feature instanceof Method) {
                return (Method) feature;
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((features == null) ? 0 : features.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
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
        Class other = (Class) obj;
        if (features == null) {
            if (other.features != null) {
                return false;
            }
        } else if (!features.equals(other.features)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (parent == null) {
            if (other.parent != null) {
                return false;
            }
        } else if (!parent.equals(other.parent)) {
            return false;
        }
        return true;
    }
}