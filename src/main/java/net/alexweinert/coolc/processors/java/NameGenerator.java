package net.alexweinert.coolc.processors.java;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

public class NameGenerator {

    private int variableCounter = 0;

    public String getJavaNameForClass(IdSymbol classId) {
        return "Cool" + classId;
    }

    public String getJavaNameForVariable(IdSymbol name) {
        return "cool" + name;
    }

    public String getFreshVariableName() {
        return "tempVar" + (variableCounter++);
    }

}
