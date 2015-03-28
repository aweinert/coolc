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

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitLoadIntInstruction(this.getLabel(), this.getTarget(), this.value);
    }

}
