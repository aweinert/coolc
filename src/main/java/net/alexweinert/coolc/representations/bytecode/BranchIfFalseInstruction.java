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

}
