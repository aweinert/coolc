package net.alexweinert.coolc.representations.cool.ast;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

import net.alexweinert.coolc.representations.cool.Utilities;
import net.alexweinert.coolc.representations.cool.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.ClassTable;
import net.alexweinert.coolc.representations.cool.symboltables.FeatureTable;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * Defines AST constructor 'class_c'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class ClassNode extends TreeNode {
    final protected IdSymbol name;
    final protected IdSymbol parent;
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
    public ClassNode(String filename, int lineNumber, IdSymbol a1, IdSymbol a2, Features a3) {
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
        dump_IdSymbol(out, n + 2, name);
        dump_IdSymbol(out, n + 2, parent);
        features.dump(out, n + 2);
        out.print(Utilities.pad(n + 2) + this.getFilename());
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_class");
        dump_IdSymbol(out, n + 2, name);
        dump_IdSymbol(out, n + 2, parent);
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

    public IdSymbol getIdentifier() {
        return name;
    }

    public IdSymbol getParent() {
        return parent;
    }

    public Features getFeatures() {
        return features;
    }

    public Attribute getAttribute(IdSymbol identifier) {
        for (Feature feature : this.features) {
            if (feature.getName().equals(identifier) && feature instanceof Attribute) {
                return (Attribute) feature;
            }
        }
        return null;
    }

    public Method getMethod(IdSymbol identifier) {
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
        ClassNode other = (ClassNode) obj;
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

    public ClassNode setParent(IdSymbol newParent) {
        return new ClassNode(this.getFilename(), this.getLineNumber(), this.name, newParent, this.features);
    }

    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.getIdentifier());
        builder.append(" inherits ");
        builder.append(this.getParent());
        builder.append(" {\n");
        final Iterator<Feature> it = this.features.iterator();
        while (it.hasNext()) {
            builder.append("\t");
            builder.append(it.next().toString());
            builder.append("\n");
        }
        builder.append("}");
        return builder.toString();
    }

    public ClassNode setFeatures(List<Feature> features2) {
        return new ClassNode(this.getFilename(), this.getLineNumber(), this.name, this.parent, new Features(
                this.features.getFilename(), this.features.getLineNumber(), features2));
    }
}