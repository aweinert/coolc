package net.alexweinert.coolc.representations.bytecode;

class LoadBoolInstruction extends AssignInstruction {

    private final boolean value;

    public LoadBoolInstruction(String label, String target, boolean value) {
        super(label, target);
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (value ? 1231 : 1237);
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
        LoadBoolInstruction other = (LoadBoolInstruction) obj;
        if (value != other.value) {
            return false;
        }
        return true;
    }

    public boolean isValue() {
        return value;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitLoadBoolInstruction(this.getLabel(), this.getTarget(), this.value);
    }

}
