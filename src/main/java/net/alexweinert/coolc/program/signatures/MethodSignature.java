package net.alexweinert.coolc.program.signatures;

import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.program.ast.Formal;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.symboltables.IdSymbol;

public class MethodSignature {
    private IdSymbol returnType;
    private IdSymbol methodIdentifier;
    private List<IdSymbol> argumentTypes;

    public static MethodSignature create(Method method) {
        final List<IdSymbol> argumentTypes = new LinkedList<>();
        for (Formal formal : method.getFormals()) {
            argumentTypes.add(formal.getDeclaredType());
        }
        return new MethodSignature(method.getReturnType(), method.getName(), argumentTypes);
    }

    MethodSignature(IdSymbol returnType, IdSymbol methodIdentifier, List<IdSymbol> argumentTypes) {
        this.returnType = returnType;
        this.methodIdentifier = methodIdentifier;
        this.argumentTypes = new LinkedList<>(argumentTypes);
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
}
