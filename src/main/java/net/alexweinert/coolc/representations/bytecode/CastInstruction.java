package net.alexweinert.coolc.representations.bytecode;

class CastInstruction extends LabeledInstruction {

    private final String variable;
    private final String type;

    public CastInstruction(String label, String variable, String type) {
        super(label);
        this.variable = variable;
        this.type = type;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitCastInstruction(this.getLabel(), this.variable, this.type);

    }

}
