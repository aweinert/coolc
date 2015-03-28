package net.alexweinert.coolc.representations.bytecode;

class LoadStringInstruction extends AssignInstruction {

    private final String value;

    public LoadStringInstruction(String label, String target, String value) {
        super(label, target);
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        LoadStringInstruction other = (LoadStringInstruction) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitLoadStringInstruction(this.getLabel(), this.getTarget(), this.value);
    }

}
