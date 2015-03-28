package net.alexweinert.coolc.representations.bytecode;

class UnconditionalBranchInstruction extends BranchInstruction {

    public UnconditionalBranchInstruction(String label, String target) {
        super(label, target);
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitBranchInstruction(this.getLabel(), this.getTarget());
    }

}
