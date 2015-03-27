package net.alexweinert.coolc.representations.bytecode;

class NewInstruction extends AssignInstruction {

    private final String type;

    public NewInstruction(String label, String result, String type) {
        super(label, result);
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
