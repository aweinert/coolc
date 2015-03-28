package net.alexweinert.coolc.representations.bytecode;

class LoadVoidInstruction extends AssignInstruction {

    public LoadVoidInstruction(String label, String target) {
        super(label, target);
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitLoadVoidInstruction(this.getLabel(), this.getTarget());
    }

}
