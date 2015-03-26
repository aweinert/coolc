package net.alexweinert.coolc.representations.bytecode;

public abstract class LabeledInstruction {
    private final String label;

    public LabeledInstruction(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
