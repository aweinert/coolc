package net.alexweinert.coolc.representations.bytecode;

abstract class AssignInstruction extends LabeledInstruction {
    private final String target;

    public AssignInstruction(String label, String target) {
        super(label);
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

}
