package net.alexweinert.coolc.representations.bytecode;

abstract class BranchInstruction extends LabeledInstruction {
    private final String target;

    public BranchInstruction(String label, String target) {
        super(label);
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

}
