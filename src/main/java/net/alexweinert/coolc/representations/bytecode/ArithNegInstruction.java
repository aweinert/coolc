package net.alexweinert.coolc.representations.bytecode;

class ArithNegInstruction extends AssignInstruction {

    private final String arg;

    public ArithNegInstruction(String label, String result, String arg) {
        super(label, result);
        this.arg = arg;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((arg == null) ? 0 : arg.hashCode());
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
        ArithNegInstruction other = (ArithNegInstruction) obj;
        if (arg == null) {
            if (other.arg != null) {
                return false;
            }
        } else if (!arg.equals(other.arg)) {
            return false;
        }
        return true;
    }

    public String getArg() {
        return arg;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitArithNegInstruction(this.getLabel(), this.getTarget(), this.arg);
    }

}
