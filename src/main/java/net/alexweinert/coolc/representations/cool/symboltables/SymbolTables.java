package net.alexweinert.coolc.representations.cool.symboltables;

public class SymbolTables {
    private final IdTable idTable;
    private final StringTable stringTable;
    private final IntTable intTable;

    public SymbolTables(IdTable idTable, StringTable stringTable, IntTable intTable) {
        this.idTable = idTable;
        this.stringTable = stringTable;
        this.intTable = intTable;
    }

    public IdTable getIdTable() {
        return idTable;
    }

    public StringTable getStringTable() {
        return stringTable;
    }

    public IntTable getIntTable() {
        return intTable;
    }
}
