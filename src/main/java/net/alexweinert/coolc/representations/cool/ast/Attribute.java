package net.alexweinert.coolc.representations.cool.ast;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * Defines AST constructor 'attr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Attribute extends Feature {
    final protected IdSymbol type_decl;
    final protected Expression init;

    /**
     * Creates "attr" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for type_decl
     * @param a2
     *            initial value for init
     */
    public Attribute(String filename, int lineNumber, IdSymbol a1, IdSymbol a2, Expression a3) {
        super(filename, lineNumber, a1);
        this.type_decl = a2;
        this.init = a3;
    }

    public IdSymbol getName() {
        return this.name;
    }

    public Expression getInitializer() {
        return this.init;
    }

    public IdSymbol getTypeDecl() {
        return this.type_decl;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitAttributePreorder(this);
        this.init.acceptVisitor(visitor);
        visitor.visitAttributePostorder(this);
    }

    public IdSymbol getDeclaredType() {
        return type_decl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((init == null) ? 0 : init.hashCode());
        result = prime * result + ((type_decl == null) ? 0 : type_decl.hashCode());
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
        Attribute other = (Attribute) obj;
        if (init == null) {
            if (other.init != null) {
                return false;
            }
        } else if (!init.equals(other.init)) {
            return false;
        }
        if (type_decl == null) {
            if (other.type_decl != null) {
                return false;
            }
        } else if (!type_decl.equals(other.type_decl)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return this.name.toString() + " : " + this.type_decl.toString() + " <- ...;";
    }

}