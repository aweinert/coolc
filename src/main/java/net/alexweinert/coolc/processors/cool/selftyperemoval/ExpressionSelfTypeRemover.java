package net.alexweinert.coolc.processors.cool.selftyperemoval;

import net.alexweinert.coolc.representations.cool.ast.Expression;
import net.alexweinert.coolc.representations.cool.ast.Visitor;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

class ExpressionSelfTypeRemover extends Visitor {
    public static Expression removeSelfType(IdSymbol containingClass, Expression expression) {
        return null;
    }
}
