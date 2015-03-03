package net.alexweinert.coolc.representations.cool.ast;

import java.io.PrintStream;

import net.alexweinert.coolc.representations.cool.Utilities;
import net.alexweinert.coolc.representations.cool.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.ClassTable;
import net.alexweinert.coolc.representations.cool.symboltables.FeatureTable;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IntSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.TreeConstants;

/**
 * Defines AST constructor 'int_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class IntConst extends Expression {
    final protected IntSymbol token;

    /**
     * Creates "int_const" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for token
     */
    public IntConst(String filename, int lineNumber, IntSymbol a1) {
        super(filename, lineNumber);
        token = a1;
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "int_const\n");
        dump_IntSymbol(out, n + 2, token);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_int");
        dump_IntSymbol(out, n + 2, token);
        dump_type(out, n);
    }

    @Override
    protected IdSymbol inferType(ClassNode enclosingClass, ClassTable classTable, FeatureTable featureTable) {
        return TreeConstants.Int;
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitIntConst(this);
    }

    public IntSymbol getValue() {
        return token;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((token == null) ? 0 : token.hashCode());
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
        IntConst other = (IntConst) obj;
        if (token == null) {
            if (other.token != null) {
                return false;
            }
        } else if (!token.equals(other.token)) {
            return false;
        }
        return true;
    }

}