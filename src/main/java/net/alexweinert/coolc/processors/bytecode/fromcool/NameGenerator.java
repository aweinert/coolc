package net.alexweinert.coolc.processors.bytecode.fromcool;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

class NameGenerator {

    private int index = 0;

    public String getIdForInitializer(IdSymbol typeSymbol, IdSymbol idSymbol) {
        return "init$" + idSymbol.getString();
    }

    public String getFreshVariableId() {
        return "genVar" + (index++);
    }

    public String getFreshLabel(String prefix) {
        return "label" + prefix + (index++);
    }
}
