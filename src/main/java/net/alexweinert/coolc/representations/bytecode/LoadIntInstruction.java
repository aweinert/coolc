package net.alexweinert.coolc.representations.bytecode;

class LoadIntInstruction extends AssignInstruction {

    private final int value;

    public LoadIntInstruction(String label, String target, int value) {
        super(label, target);
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + value;
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
        LoadIntInstruction other = (LoadIntInstruction) obj;
        if (value != other.value) {
            return false;
        }
        return true;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitLoadIntInstruction(this.getLabel(), this.getTarget(), this.value);
    }

}
