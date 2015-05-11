package net.alexweinert.coolc.representations.bytecode;

class LoadVariableInstruction extends AssignInstruction {

    private final String source;

    public LoadVariableInstruction(String label, String target, String source) {
        super(label, target);
        this.source = source;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((source == null) ? 0 : source.hashCode());
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
        LoadVariableInstruction other = (LoadVariableInstruction) obj;
        if (source == null) {
            if (other.source != null) {
                return false;
            }
        } else if (!source.equals(other.source)) {
            return false;
        }
        return true;
    }

    public String getSource() {
        return source;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitLoadVariableInstruction(this.getLabel(), this.getTarget(), this.source);
    }

    public String toString() {
        return this.getLabel() + ": " + this.getTarget() + " = " + this.getSource();
    }

}
