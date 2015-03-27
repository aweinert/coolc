package net.alexweinert.coolc.representations.bytecode;

class LteInstruction extends AssignInstruction {

    private final String lhs, rhs;

    public LteInstruction(String label, String result, String lhs, String rhs) {
        super(label, result);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String getLhs() {
        return lhs;
    }

    public String getRhs() {
        return rhs;
    }

}
