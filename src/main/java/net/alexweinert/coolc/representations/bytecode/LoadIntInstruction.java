package net.alexweinert.coolc.representations.bytecode;

class LoadIntInstruction extends AssignInstruction {

    private final int value;

    public LoadIntInstruction(String label, String target, int value) {
        super(label, target);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
