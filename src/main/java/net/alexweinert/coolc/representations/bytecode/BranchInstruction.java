package net.alexweinert.coolc.representations.bytecode;

public abstract class BranchInstruction extends LabeledInstruction {
    private final String target;

    public BranchInstruction(String label, String target) {
        super(label);
        this.target = target;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((target == null) ? 0 : target.hashCode());
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
        BranchInstruction other = (BranchInstruction) obj;
        if (target == null) {
            if (other.target != null) {
                return false;
            }
        } else if (!target.equals(other.target)) {
            return false;
        }
        return true;
    }

    public String getTarget() {
        return target;
    }

}
