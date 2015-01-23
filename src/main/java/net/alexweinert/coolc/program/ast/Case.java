package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;

/**
 * Defines AST constructor 'branch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Case extends TreeNode {
    final protected AbstractSymbol name;
    final protected AbstractSymbol type_decl;
    final protected Expression expr;

    /**
     * Creates "branch" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for type_decl
     * @param a2
     *            initial value for expr
     */
    public Case(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        this.name = a1;
        this.type_decl = a2;
        this.expr = a3;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "branch\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        expr.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_branch");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        expr.dump_with_types(out, n + 2);
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitCasePreorder(this);
        this.expr.acceptVisitor(visitor);
        visitor.visitCasePostorder(this);
    }

    public AbstractSymbol getVariableIdentifier() {
        return name;
    }

    public AbstractSymbol getDeclaredType() {
        return type_decl;
    }

}