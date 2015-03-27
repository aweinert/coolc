package net.alexweinert.coolc.representations.bytecode;

class IsVoidInstruction extends AssignInstruction {

    private final String arg;

    public IsVoidInstruction(String label, String result, String arg) {
        super(label, result);
        this.arg = arg;
    }

    public String getArg() {
        return arg;
    }

}
