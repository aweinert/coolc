package net.alexweinert.coolc.representations.bytecode;

class ArithNegInstruction extends AssignInstruction {

    private final String arg;

    public ArithNegInstruction(String label, String result, String arg) {
        super(label, result);
        this.arg = arg;
    }

    public String getArg() {
        return arg;
    }

}
