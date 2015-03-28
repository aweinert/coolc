package net.alexweinert.coolc.representations.bytecode;

class NewInstruction extends AssignInstruction {

    private final String type;

    public NewInstruction(String label, String result, String type) {
        super(label, result);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitNewInstruction(this.getLabel(), this.getTarget(), this.type);
    }

}
