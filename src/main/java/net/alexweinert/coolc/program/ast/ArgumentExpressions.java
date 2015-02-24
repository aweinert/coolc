package net.alexweinert.coolc.program.ast;

import java.util.Collection;
import java.util.Iterator;

import net.alexweinert.coolc.program.ast.visitors.ASTVisitor;

public class ArgumentExpressions extends Expressions {
    public ArgumentExpressions(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    public ArgumentExpressions(String filename, int lineNumber, Collection<Expression> elements) {
        super(filename, lineNumber, elements);
    }

    @Override
    public ArgumentExpressions add(Expression node) {
        final Collection<Expression> newElements = this.copyElements();
        newElements.add(node);
        return new ArgumentExpressions(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public ArgumentExpressions remove(Expression node) {
        final Collection<Expression> newElements = this.copyElements();
        newElements.remove(node);
        return new ArgumentExpressions(this.getFilename(), this.getLineNumber(), newElements);
    }

    @Override
    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visitArgumentExpressionsPreorder(this);
        final Iterator<Expression> iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().acceptVisitor(visitor);
            if (iterator.hasNext()) {
                visitor.visitArgumentExpressionsInorder(this);
            }
        }
        visitor.visitArgumentExpressionsPostorder(this);
    }

}
