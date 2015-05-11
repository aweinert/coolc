package net.alexweinert.coolc.representations.bytecode;

class ReturnInstruction extends LabeledInstruction {

    private final String returnVariable;

    public ReturnInstruction(String label, String returnVariable) {
        super(label);
        this.returnVariable = returnVariable;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((returnVariable == null) ? 0 : returnVariable.hashCode());
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
        ReturnInstruction other = (ReturnInstruction) obj;
        if (returnVariable == null) {
            if (other.returnVariable != null) {
                return false;
            }
        } else if (!returnVariable.equals(other.returnVariable)) {
            return false;
        }
        return true;
    }

    public String getReturnVariable() {
        return returnVariable;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitReturnInstruction(this.getLabel(), this.returnVariable);
    }

    public String toString() {
        return this.getLabel() + ": return " + this.returnVariable;
    }
}
