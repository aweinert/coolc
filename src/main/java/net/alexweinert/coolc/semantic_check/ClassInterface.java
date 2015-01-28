package net.alexweinert.coolc.semantic_check;

import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.program.symboltables.AbstractSymbol;

class ClassInterface {
    final private AbstractSymbol classIdentifier;
    final private List<MethodSignature> methods;

    ClassInterface(AbstractSymbol classIdentifier, List<MethodSignature> methods) {
        this.classIdentifier = classIdentifier;
        this.methods = methods;
    }

    public AbstractSymbol getClassIdentifier() {
        return classIdentifier;
    }

    public List<MethodSignature> getMethods() {
        return new LinkedList<>(methods);
    }
}
