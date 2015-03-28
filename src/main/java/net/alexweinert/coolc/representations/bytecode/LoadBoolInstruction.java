package net.alexweinert.coolc.representations.bytecode;

class LoadBoolInstruction extends AssignInstruction {

    private final boolean value;

    public LoadBoolInstruction(String label, String target, boolean value) {
        super(label, target);
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitLoadBoolInstruction(this.getLabel(), this.getTarget(), this.value);
    }

}
