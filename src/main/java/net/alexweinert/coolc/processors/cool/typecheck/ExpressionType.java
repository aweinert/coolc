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

        @Override
        public boolean conformsTo(ExpressionType other, ClassHierarchy hierarchy) {
            return other.equals(this) || hierarchy.conformsTo(this.containingClass, other.getTypeId());
        }

        public boolean equals(Object other) {
            return other.getClass().equals(this.getClass())
                    && this.containingClass.equals(((SelfType) other).containingClass);
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

        @Override
        public boolean conformsTo(ExpressionType other, ClassHierarchy hierarchy) {
            return hierarchy.conformsTo(this.type, other.getTypeId());
        }
    }

    private static class NoExpressionType extends ExpressionType {

        @Override
        public IdSymbol getTypeId() {
            return IdTable.getInstance().getNoExprTypeSymbol();
        }

        @Override
        public boolean conformsTo(ExpressionType other, ClassHierarchy hierarchy) {
            return true;
        }

    }

    public static ExpressionType createObjectType() {
        return new ConcreteType(IdTable.getInstance().getObjectSymbol());
    }

    public static ExpressionType createIntType() {
        return new ConcreteType(IdTable.getInstance().getIntSymbol());
    }

    public static ExpressionType createBoolType() {
        return new ConcreteType(IdTable.getInstance().getBoolSymbol());
    }

    public static ExpressionType createStringType() {
        return new ConcreteType(IdTable.getInstance().getStringSymbol());
    }

    public static ExpressionType create(IdSymbol type, IdSymbol containingClass) {
        if (type.equals(IdTable.getInstance().addString("SELF_TYPE"))) {
            return new SelfType(containingClass);
        } else {
            return new ConcreteType(type);
        }
    }

    public static ExpressionType createNoExpressionType() {
        return new NoExpressionType();
    }

    public abstract IdSymbol getTypeId();

    public abstract boolean conformsTo(ExpressionType other, ClassHierarchy hierarchy);

    public ExpressionType computeLeastUpperBound(ExpressionType other, IdSymbol containingClassId,
            ClassHierarchy hierarchy) {
        final IdSymbol leastUpperBoundSymbol = hierarchy.getLeastUpperBound(this.getTypeId(), other.getTypeId());
        return new ConcreteType(leastUpperBoundSymbol);
    }
}
