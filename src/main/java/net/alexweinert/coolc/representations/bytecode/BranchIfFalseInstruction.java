package net.alexweinert.coolc.representations.bytecode;

class BranchIfFalseInstruction extends BranchInstruction {

    private final String conditionVariable;

    public BranchIfFalseInstruction(String label, String conditionVariable, String target) {
        super(label, target);
        this.conditionVariable = conditionVariable;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((conditionVariable == null) ? 0 : conditionVariable.hashCode());
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
        BranchIfFalseInstruction other = (BranchIfFalseInstruction) obj;
        if (conditionVariable == null) {
            if (other.conditionVariable != null) {
                return false;
            }
        } else if (!conditionVariable.equals(other.conditionVariable)) {
            return false;
        }
        return true;
    }

    public String getConditionVariable() {
        return conditionVariable;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitBranchIfFalseInstruction(this.getLabel(), this.getTarget(), this.conditionVariable);
    }

}
