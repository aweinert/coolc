package net.alexweinert.coolc.representations.bytecode;

class BinaryArithmeticInstruction extends AssignInstruction {

    private enum BinArithType {
        ADD("+"), SUB("-"), MUL("*"), DIV("/");

        private final String symbol;

        private BinArithType(String symbol) {
            this.symbol = symbol;
        }

        public String toString() {
            return this.symbol;
        }
    }

    public static BinaryArithmeticInstruction createAddInstruction(String label, String target, String lhs, String rhs) {
        return new BinaryArithmeticInstruction(label, target, lhs, rhs, BinArithType.ADD);
    }

    public static BinaryArithmeticInstruction createSubInstruction(String label, String target, String lhs, String rhs) {
        return new BinaryArithmeticInstruction(label, target, lhs, rhs, BinArithType.SUB);
    }

    public static BinaryArithmeticInstruction createMulInstruction(String label, String target, String lhs, String rhs) {
        return new BinaryArithmeticInstruction(label, target, lhs, rhs, BinArithType.MUL);
    }

    public static BinaryArithmeticInstruction createDivInstruction(String label, String target, String lhs, String rhs) {
        return new BinaryArithmeticInstruction(label, target, lhs, rhs, BinArithType.DIV);
    }

    private final BinArithType type;

    private final String lhs, rhs;

    public BinaryArithmeticInstruction(String label, String target, String lhs, String rhs, BinArithType type) {
        super(label, target);
        this.type = type;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String toString() {
        return String.format("%s = %s %s %s", this.getTarget(), this.lhs, this.type, this.rhs);
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        switch (this.type) {
        case ADD:
            visitor.visitAddInstruction(this.getLabel(), this.getTarget(), this.lhs, this.rhs);
            break;
        case DIV:
            visitor.visitDivInstruction(this.getLabel(), this.getTarget(), this.lhs, this.rhs);
            break;
        case MUL:
            visitor.visitMulInstruction(this.getLabel(), this.getTarget(), this.lhs, this.rhs);
            break;
        case SUB:
            visitor.visitSubInstruction(this.getLabel(), this.getTarget(), this.lhs, this.rhs);
            break;
        default:
            assert false;
            break;

        }

    }

}
