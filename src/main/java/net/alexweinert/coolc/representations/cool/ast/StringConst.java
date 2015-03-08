package net.alexweinert.coolc.representations.cool.ast;

import net.alexweinert.coolc.representations.cool.ast.visitors.ASTVisitor;
import net.alexweinert.coolc.representations.cool.symboltables.StringSymbol;

/**
 * Defines AST constructor 'string_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
public class StringConst extends Expression {
    final protected StringSymbol token;

    /**
     * Creates "string_const" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for token
     */
    public StringConst(String filename, int lineNumber, StringSymbol a1) {
        super(filename, lineNumber);
        token = a1;
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitStringConstant(this);
    }

    public StringSymbol getValue() {
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
        StringConst other = (StringConst) obj;
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