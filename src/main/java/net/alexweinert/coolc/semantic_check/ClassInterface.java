package net.alexweinert.coolc.semantic_check;

import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.program.symboltables.IdSymbol;

class ClassInterface {
    final private IdSymbol classIdentifier;
    final private List<MethodSignature> methods;

    ClassInterface(IdSymbol classIdentifier, List<MethodSignature> methods) {
        this.classIdentifier = classIdentifier;
        this.methods = methods;
    }

    public IdSymbol getClassIdentifier() {
        return classIdentifier;
    }

    public List<MethodSignature> getMethods() {
        return new LinkedList<>(methods);
    }
}
