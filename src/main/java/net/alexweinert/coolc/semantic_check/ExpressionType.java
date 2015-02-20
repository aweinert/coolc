package net.alexweinert.coolc.semantic_check;

import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

abstract class ExpressionType {
    private static class SelfType extends ExpressionType {
        public IdSymbol getTypeId(IdSymbol containingClass) {
            return containingClass;
        }
    }

    private static class ConcreteType extends ExpressionType {
        final private IdSymbol type;

        private ConcreteType(IdSymbol type) {
            this.type = type;
        }

        public IdSymbol getTypeId(IdSymbol containingClass) {
            return this.type;
        }
    }

    public static ExpressionType create(IdSymbol type) {
        if (type.equals(IdTable.getInstance().addString("SELF_TYPE"))) {
            return new SelfType();
        } else {
            return new ConcreteType(type);
        }
    }

    public abstract IdSymbol getTypeId(IdSymbol containingClass);
}
