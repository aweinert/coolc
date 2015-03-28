package net.alexweinert.coolc.representations.bytecode;

class NewInstruction extends AssignInstruction {

    private final String type;

    public NewInstruction(String label, String result, String type) {
        super(label, result);
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        NewInstruction other = (NewInstruction) obj;
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

    public String getType() {
        return type;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitNewInstruction(this.getLabel(), this.getTarget(), this.type);
    }

}
