package net.alexweinert.coolc.processors.cool.typecheck;

import net.alexweinert.coolc.representations.cool.information.ClassHierarchy;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

abstract class ExpressionType {
    private static class SelfType extends ExpressionType {
        public IdSymbol getTypeId(IdSymbol containingClass) {
            return containingClass;
        }

        @Override
        public ExpressionType computeLeastUpperBound(ExpressionType other, IdSymbol containingClassId,
                ClassHierarchy hierarchy) {
            if (other instanceof SelfType) {
                return this;
            } else {
                return other.computeLeastUpperBound(this, containingClassId, hierarchy);
            }
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

        public IdSymbol getTypeId(IdSymbol containingClass) {
            return this.type;
        }

        @Override
        public ExpressionType computeLeastUpperBound(ExpressionType other, IdSymbol containingClassId,
                ClassHierarchy hierarchy) {
            final IdSymbol leastUpperBoundSymbol = hierarchy.getLeastUpperBound(this.type,
                    other.getTypeId(containingClassId));
            return ExpressionType.create(leastUpperBoundSymbol);
        }

        @Override
        public String toString() {
            return this.type.toString();
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

    public abstract ExpressionType computeLeastUpperBound(ExpressionType other, IdSymbol containingClassId,
            ClassHierarchy hierarchy);
}
