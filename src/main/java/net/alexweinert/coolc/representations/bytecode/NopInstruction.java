package net.alexweinert.coolc.representations.bytecode;

class NopInstruction extends LabeledInstruction {

    public NopInstruction(String label) {
        super(label);
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitNopInstruction(this.getLabel());

    }

}
