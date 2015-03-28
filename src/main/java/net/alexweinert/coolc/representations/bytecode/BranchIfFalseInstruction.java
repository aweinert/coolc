package net.alexweinert.coolc.representations.bytecode;

class BranchIfFalseInstruction extends BranchInstruction {

    private final String conditionVariable;

    public BranchIfFalseInstruction(String label, String conditionVariable, String target) {
        super(label, target);
        this.conditionVariable = conditionVariable;
    }

    public String getConditionVariable() {
        return conditionVariable;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitBranchIfFalseInstruction(this.getLabel(), this.getTarget(), this.conditionVariable);
    }

}
