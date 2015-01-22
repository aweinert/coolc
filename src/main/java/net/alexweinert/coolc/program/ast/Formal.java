package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;

/**
 * Defines AST constructor 'formalc'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Formal extends TreeNode {
    final protected AbstractSymbol name;
    final protected AbstractSymbol type_decl;

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
    public Formal(int lineNumber, AbstractSymbol a1, AbstractSymbol a2) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "formalc\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_formal");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }

    public AbstractSymbol getName() {
        return this.name;
    }

    public AbstractSymbol getTypeDecl() {
        return this.type_decl;
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitFormal(this);
    }

}