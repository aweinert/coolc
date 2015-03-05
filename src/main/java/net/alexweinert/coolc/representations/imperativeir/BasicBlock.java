package net.alexweinert.coolc.representations.imperativeir;

import java.util.List;

import net.alexweinert.coolc.representations.imperativeir.instructions.Instruction;

public class BasicBlock {
    private final String identifier;
    private final List<Instruction> instructions;

    public BasicBlock(String identifier, List<Instruction> instructions) {
        this.identifier = identifier;
        this.instructions = instructions;
    }
}
