package net.alexweinert.coolc.processors.cool.hierarchycheck;

import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.representations.cool.program.hierarchichal.MethodSignature;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

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
