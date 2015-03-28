package net.alexweinert.coolc.representations.bytecode;

class EqInstruction extends AssignInstruction {

    private final String lhs, rhs;

    public EqInstruction(String label, String result, String lhs, String rhs) {
        super(label, result);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((lhs == null) ? 0 : lhs.hashCode());
        result = prime * result + ((rhs == null) ? 0 : rhs.hashCode());
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
        EqInstruction other = (EqInstruction) obj;
        if (lhs == null) {
            if (other.lhs != null) {
                return false;
            }
        } else if (!lhs.equals(other.lhs)) {
            return false;
        }
        if (rhs == null) {
            if (other.rhs != null) {
                return false;
            }
        } else if (!rhs.equals(other.rhs)) {
            return false;
        }
        return true;
    }

    public String getLhs() {
        return lhs;
    }

    public String getRhs() {
        return rhs;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitEqInstruction(this.getLabel(), this.getTarget(), this.lhs, this.rhs);
    }

}
