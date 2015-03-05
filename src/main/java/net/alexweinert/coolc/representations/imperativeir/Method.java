package net.alexweinert.coolc.representations.imperativeir;

import java.util.List;

public class Method {
    private final List<BasicBlock> basicBlocks;

    public Method(List<BasicBlock> basicBlocks) {
        this.basicBlocks = basicBlocks;
    }
}
