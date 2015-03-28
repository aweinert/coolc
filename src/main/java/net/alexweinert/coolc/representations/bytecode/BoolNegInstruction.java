package net.alexweinert.coolc.representations.bytecode;

class BoolNegInstruction extends AssignInstruction {

    private final String arg;

    public BoolNegInstruction(String label, String result, String arg) {
        super(label, result);
        this.arg = arg;
    }

    public String getArg() {
        return arg;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitBoolNegInstruction(this.getLabel(), this.getTarget(), this.arg);
    }

}
