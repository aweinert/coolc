package net.alexweinert.coolc.processors.cool.typecheck;

import net.alexweinert.coolc.representations.cool.information.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

abstract class ExpressionType {
    private static class SelfType extends ExpressionType {

        final private IdSymbol containingClass;

        public SelfType(IdSymbol containingClass) {
            this.containingClass = containingClass;
        }

        public IdSymbol getTypeId() {
            return this.containingClass;
        }

        @Override
        public String toString() {
            return "SELF_TYPE";
        }
    }

    private static class ConcreteType extends ExpressionType {
        final private IdSymbol type;

        private ConcreteType(IdSymbol type) {
            this.type = type;
        }

        public IdSymbol getTypeId() {
            return this.type;
        }

        @Override
        public String toString() {
            return this.type.toString();
        }
    }

    public static ExpressionType create(IdSymbol type, IdSymbol containingClass) {
        if (type.equals(IdTable.getInstance().addString("SELF_TYPE"))) {
            return new SelfType(containingClass);
        } else {
            return new ConcreteType(type);
        }
    }

    public abstract IdSymbol getTypeId();

    public ExpressionType computeLeastUpperBound(ExpressionType other, IdSymbol containingClassId,
            ClassHierarchy hierarchy) {
        final IdSymbol leastUpperBoundSymbol = hierarchy.getLeastUpperBound(this.getTypeId(), other.getTypeId());
        return new ConcreteType(leastUpperBoundSymbol);
    }
}
