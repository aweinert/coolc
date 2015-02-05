package net.alexweinert.coolc.semantic_check;

import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.program.symboltables.IdSymbol;

class MethodSignature {
    private IdSymbol returnType;
    private IdSymbol methodIdentifier;
    private List<IdSymbol> argumentTypes;
    private IdSymbol definingClass;

    MethodSignature(IdSymbol returnType, IdSymbol methodIdentifier, List<IdSymbol> argumentTypes,
            IdSymbol definingClass) {
        this.returnType = returnType;
        this.methodIdentifier = methodIdentifier;
        this.argumentTypes = new LinkedList<>(argumentTypes);
        this.definingClass = definingClass;
    }

    public IdSymbol getReturnType() {
        return returnType;
    }

    public IdSymbol getIdentifier() {
        return this.methodIdentifier;
    }

    public List<IdSymbol> getArgumentTypes() {
        return new LinkedList<>(argumentTypes);
    }

    public IdSymbol getDefiningClass() {
        return definingClass;
    }
}
