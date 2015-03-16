package net.alexweinert.coolc.processors.java.variablerenaming;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

class NameGenerator {
    private int index;

    public IdSymbol getNewSymbol() {
        return IdTable.getInstance().addString("genVar" + index);
    }
}
