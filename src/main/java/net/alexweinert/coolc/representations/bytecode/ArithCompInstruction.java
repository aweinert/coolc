package net.alexweinert.coolc.representations.bytecode;

class ArithCompInstruction extends AssignInstruction {

    private enum CompType {
        LT("<"), LTE("<=");

        private final String symbol;

        private CompType(String symbol) {
            this.symbol = symbol;
        }

        public String toString() {
            return this.symbol;
        }
    }

    private final String lhs, rhs;

    private final CompType type;

    public static ArithCompInstruction createLtInstruction(String label, String target, String lhs, String rhs) {
        return new ArithCompInstruction(label, target, lhs, rhs, CompType.LT);
    }

    public static ArithCompInstruction createLteInstruction(String label, String target, String lhs, String rhs) {
        return new ArithCompInstruction(label, target, lhs, rhs, CompType.LTE);
    }

    public ArithCompInstruction(String label, String target, String lhs, String rhs, CompType type) {
        super(label, target);
        this.lhs = lhs;
        this.rhs = rhs;
        this.type = type;
    }

    public String getLhs() {
        return lhs;
    }

    public String getRhs() {
        return rhs;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        switch (this.type) {
        case LT:
            visitor.visitLtInstruction(this.getLabel(), this.getTarget(), this.lhs, this.rhs);
            break;
        case LTE:
            visitor.visitLteInstruction(this.getLabel(), this.getTarget(), this.lhs, this.rhs);
            break;
        default:
            assert false;
            break;
        }

    }
}
