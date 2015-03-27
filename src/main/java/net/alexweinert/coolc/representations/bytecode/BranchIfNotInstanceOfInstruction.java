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

}
