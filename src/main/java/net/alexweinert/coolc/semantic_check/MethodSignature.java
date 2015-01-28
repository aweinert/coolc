package net.alexweinert.coolc.semantic_check;

import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.program.symboltables.AbstractSymbol;

class MethodSignature {
    private AbstractSymbol returnType;
    private AbstractSymbol methodIdentifier;
    private List<AbstractSymbol> argumentTypes;
    private AbstractSymbol definingClass;

    MethodSignature(AbstractSymbol returnType, AbstractSymbol methodIdentifier, List<AbstractSymbol> argumentTypes,
            AbstractSymbol definingClass) {
        this.returnType = returnType;
        this.methodIdentifier = methodIdentifier;
        this.argumentTypes = new LinkedList<>(argumentTypes);
        this.definingClass = definingClass;
    }

    public AbstractSymbol getReturnType() {
        return returnType;
    }

    public AbstractSymbol getIdentifier() {
        return this.methodIdentifier;
    }

    public List<AbstractSymbol> getArgumentTypes() {
        return new LinkedList<>(argumentTypes);
    }

    public AbstractSymbol getDefiningClass() {
        return definingClass;
    }
}
