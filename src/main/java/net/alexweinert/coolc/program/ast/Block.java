package net.alexweinert.coolc.program.ast;

import java.io.PrintStream;
import java.util.Enumeration;

import net.alexweinert.coolc.program.Utilities;
import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.program.symboltables.AbstractSymbol;
import net.alexweinert.coolc.program.symboltables.ClassTable;
import net.alexweinert.coolc.program.symboltables.FeatureTable;
import net.alexweinert.coolc.program.symboltables.TreeConstants;

/**
 * Defines AST constructor 'block'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class Block extends Expression {
    final protected Expressions body;

    /**
     * Creates "block" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for body
     */
    public Block(String filename, int lineNumber, Expressions a1) {
        super(filename, lineNumber);
        this.body = a1;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "block\n");
        body.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_block");
        for (final Expression e : this.body) {
            e.dump_with_types(out, n + 2);
        }
        dump_type(out, n);
    }

    @Override
    protected AbstractSymbol inferType(Class enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        AbstractSymbol blockType = TreeConstants.No_type;
        for (final Expression currentExpression : this.body) {
            blockType = currentExpression.typecheck(enclosingClass, classTable, featureTable);
        }
        return blockType;
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitBlockPreorder(this);
        this.body.acceptVisitor(visitor);
        visitor.visitBlockPostorder(this);
    }

}