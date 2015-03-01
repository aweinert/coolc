package net.alexweinert.coolc.program.information;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.program.ast.Formal;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

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

    public static MethodSignature createObjectAbortSignature() {
        final IdSymbol returnType = IdTable.getInstance().getObjectSymbol();
        final IdSymbol identifierSymbol = IdTable.getInstance().addString("abort");
        return new MethodSignature(returnType, identifierSymbol, Collections.<IdSymbol> emptyList());
    }

    public static MethodSignature createObjectTypeNameSignature() {
        final IdSymbol returnType = IdTable.getInstance().getStringSymbol();
        final IdSymbol identifierSymbol = IdTable.getInstance().addString("type_name");
        return new MethodSignature(returnType, identifierSymbol, Collections.<IdSymbol> emptyList());
    }

    public static MethodSignature createObjectCopySignature() {
        final IdSymbol returnType = IdTable.getInstance().getSelfTypeSymbol();
        final IdSymbol identifierSymbol = IdTable.getInstance().addString("copy");
        return new MethodSignature(returnType, identifierSymbol, Collections.<IdSymbol> emptyList());
    }

    public static MethodSignature createStringLengthSignature() {
        final IdSymbol returnType = IdTable.getInstance().getIntSymbol();
        final IdSymbol identifierSymbol = IdTable.getInstance().addString("length");
        return new MethodSignature(returnType, identifierSymbol, Collections.<IdSymbol> emptyList());
    }

    public static MethodSignature createStringConcatSignature() {
        final IdSymbol returnType = IdTable.getInstance().getStringSymbol();
        final IdSymbol identifierSymbol = IdTable.getInstance().addString("concat");
        final List<IdSymbol> argumentTypes = new LinkedList<>();
        argumentTypes.add(IdTable.getInstance().getStringSymbol());
        return new MethodSignature(returnType, identifierSymbol, argumentTypes);
    }

    public static MethodSignature createStringSubstrSignature() {
        final IdSymbol returnType = IdTable.getInstance().getStringSymbol();
        final IdSymbol identifierSymbol = IdTable.getInstance().addString("substr");
        final List<IdSymbol> argumentTypes = new LinkedList<>();
        argumentTypes.add(IdTable.getInstance().getIntSymbol());
        argumentTypes.add(IdTable.getInstance().getIntSymbol());
        return new MethodSignature(returnType, identifierSymbol, argumentTypes);
    }

    public static MethodSignature createIOOutStringSignature() {
        final IdSymbol returnType = IdTable.getInstance().getSelfTypeSymbol();
        final IdSymbol identifierSymbol = IdTable.getInstance().addString("out_string");
        final List<IdSymbol> argumentTypes = new LinkedList<>();
        argumentTypes.add(IdTable.getInstance().getStringSymbol());
        return new MethodSignature(returnType, identifierSymbol, argumentTypes);
    }

    public static MethodSignature createIOOutIntSignature() {
        final IdSymbol returnType = IdTable.getInstance().getSelfTypeSymbol();
        final IdSymbol identifierSymbol = IdTable.getInstance().addString("out_int");
        final List<IdSymbol> argumentTypes = new LinkedList<>();
        argumentTypes.add(IdTable.getInstance().getStringSymbol());
        return new MethodSignature(returnType, identifierSymbol, argumentTypes);
    }

    public static MethodSignature createIOInStringSignature() {
        final IdSymbol returnType = IdTable.getInstance().getStringSymbol();
        final IdSymbol identifierSymbol = IdTable.getInstance().addString("in_string");
        return new MethodSignature(returnType, identifierSymbol, Collections.<IdSymbol> emptyList());
    }

    public static MethodSignature createIOInIntSignature() {
        final IdSymbol returnType = IdTable.getInstance().getIntSymbol();
        final IdSymbol identifierSymbol = IdTable.getInstance().addString("in_int");
        return new MethodSignature(returnType, identifierSymbol, Collections.<IdSymbol> emptyList());
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
