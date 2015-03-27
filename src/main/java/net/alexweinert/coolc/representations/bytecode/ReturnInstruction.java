package net.alexweinert.coolc.representations.bytecode;

class ReturnInstruction extends LabeledInstruction {

    private final String returnVariable;

    public ReturnInstruction(String label, String returnVariable) {
        super(label);
        this.returnVariable = returnVariable;
    }

    public String getReturnVariable() {
        return returnVariable;
    }

}
