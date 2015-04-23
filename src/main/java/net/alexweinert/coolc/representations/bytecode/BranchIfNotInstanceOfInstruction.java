package net.alexweinert.coolc.representations.bytecode;

class BranchIfNotInstanceOfInstruction extends BranchInstruction {

    private final String expressionVariable;
    private final String type;

    public BranchIfNotInstanceOfInstruction(String label, String target, String expressionVariable, String type) {
        super(label, target);
        this.expressionVariable = expressionVariable;
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((expressionVariable == null) ? 0 : expressionVariable.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BranchIfNotInstanceOfInstruction other = (BranchIfNotInstanceOfInstruction) obj;
        if (expressionVariable == null) {
            if (other.expressionVariable != null) {
                return false;
            }
        } else if (!expressionVariable.equals(other.expressionVariable)) {
            return false;
        }
        return true;
    }

    public String getExpressionVariable() {
        return expressionVariable;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitBranchIfNotInstanceOfInstruction(this.getLabel(), this.getTarget(), this.expressionVariable,
                this.type);
    }

}
