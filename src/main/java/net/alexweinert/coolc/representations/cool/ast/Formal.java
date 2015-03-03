package net.alexweinert.coolc.representations.cool.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.representations.cool.Utilities;
import net.alexweinert.coolc.representations.cool.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

/**
 * Defines AST constructor 'formalc'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Formal extends TreeNode {
    final protected IdSymbol name;
    final protected IdSymbol type_decl;

    /**
     * Creates "formalc" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for type_decl
     */
    public Formal(String filename, int lineNumber, IdSymbol a1, IdSymbol a2) {
        super(filename, lineNumber);
        name = a1;
        type_decl = a2;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "formalc\n");
        dump_IdSymbol(out, n + 2, name);
        dump_IdSymbol(out, n + 2, type_decl);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_formal");
        dump_IdSymbol(out, n + 2, name);
        dump_IdSymbol(out, n + 2, type_decl);
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitFormal(this);
    }

    public IdSymbol getIdentifier() {
        return this.name;
    }

    public IdSymbol getDeclaredType() {
        return this.type_decl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type_decl == null) ? 0 : type_decl.hashCode());
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
        Formal other = (Formal) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
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
}