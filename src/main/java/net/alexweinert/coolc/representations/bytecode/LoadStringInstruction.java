package net.alexweinert.coolc.representations.bytecode;

class LoadStringInstruction extends AssignInstruction {

    private final String value;

    public LoadStringInstruction(String label, String target, String value) {
        super(label, target);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitLoadStringInstruction(this.getLabel(), this.getTarget(), this.value);
    }

}
