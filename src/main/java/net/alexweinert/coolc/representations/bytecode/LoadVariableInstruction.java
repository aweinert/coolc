package net.alexweinert.coolc.representations.bytecode;

class LoadVariableInstruction extends AssignInstruction {

    private final String source;

    public LoadVariableInstruction(String label, String target, String source) {
        super(label, target);
        this.source = source;
    }

    public String getSource() {
        return source;
    }

}
