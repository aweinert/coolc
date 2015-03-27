package net.alexweinert.coolc.representations.bytecode;

class UnconditionalBranchInstruction extends BranchInstruction {

    public UnconditionalBranchInstruction(String label, String target) {
        super(label, target);
    }

}
