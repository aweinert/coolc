package net.alexweinert.coolc.representations.cool.ast;

import java.util.Iterator;
import java.util.List;

import net.alexweinert.coolc.representations.cool.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * The complete program. Root node of a well-formed AST
 */
public class Program extends TreeNode {
    protected final Classes classes;

    /**
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param classes
     *            initial value for classes
     */
    public Program(String filename, int lineNumber, Classes classes) {
        super(filename, lineNumber);
        this.classes = classes;
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

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitProgramPreorder(this);
        this.classes.acceptVisitor(visitor);
        visitor.visitProgramPostorder(this);
    }

    public Classes getClasses() {
        return this.classes;
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
        Program other = (Program) obj;
        if (classes == null) {
            if (other.classes != null) {
                return false;
            }
        } else if (!classes.equals(other.classes)) {
            return false;
        }
        return true;
    }

    public Program setClasses(Classes newClasses) {
        return new Program(this.getFilename(), this.getLineNumber(), newClasses);
    }

    public Program setClasses(List<ClassNode> newClasses) {
        return this.setClasses(new Classes(this.classes.getFilename(), this.classes.getLineNumber(), newClasses));
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