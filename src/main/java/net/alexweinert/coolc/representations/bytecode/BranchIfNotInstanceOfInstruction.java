package net.alexweinert.coolc.representations.bytecode;

class BranchIfNotInstanceOfInstruction extends BranchInstruction {

    private final String expressionVariable;

    public BranchIfNotInstanceOfInstruction(String label, String expressionVariable, String target) {
        super(label, target);
        this.expressionVariable = expressionVariable;
    }

    public String getExpressionVariable() {
        return expressionVariable;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitBranchIfNotInstanceOfInstruction(this.getLabel(), this.getTarget(), this.expressionVariable);
    }

}
