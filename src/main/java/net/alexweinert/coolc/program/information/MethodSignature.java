package net.alexweinert.coolc.program.information;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((argumentTypes == null) ? 0 : argumentTypes.hashCode());
        result = prime * result + ((methodIdentifier == null) ? 0 : methodIdentifier.hashCode());
        result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MethodSignature other = (MethodSignature) obj;
        if (argumentTypes == null) {
            if (other.argumentTypes != null) {
                return false;
            }
        } else if (!argumentTypes.equals(other.argumentTypes)) {
            return false;
        }
        if (methodIdentifier == null) {
            if (other.methodIdentifier != null) {
                return false;
            }
        } else if (!methodIdentifier.equals(other.methodIdentifier)) {
            return false;
        }
        if (returnType == null) {
            if (other.returnType != null) {
                return false;
            }
        } else if (!returnType.equals(other.returnType)) {
            return false;
        }
        return true;
    }
}
