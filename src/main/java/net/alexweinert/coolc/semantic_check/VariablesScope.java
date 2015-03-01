package net.alexweinert.coolc.semantic_check;

import java.util.HashMap;
import java.util.Map;

import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.ast.Formal;
import net.alexweinert.coolc.program.ast.Method;
import net.alexweinert.coolc.program.information.DefinedClassSignature;
import net.alexweinert.coolc.program.symboltables.IdSymbol;
import net.alexweinert.coolc.program.symboltables.IdTable;

class VariablesScope {
    final private Map<IdSymbol, ExpressionType> variableTypes;

    public static VariablesScope createFromClassSignature(IdSymbol classId, DefinedClassSignature signature) {
        final Map<IdSymbol, ExpressionType> types = new HashMap<>();
        types.put(IdTable.getInstance().getSelfSymbol(), ExpressionType.create(classId));
        for (Attribute attribute : signature.getAttributes()) {
            types.put(attribute.getName(), ExpressionType.create(attribute.getDeclaredType()));
        }
        return new VariablesScope(types);
    }

    public static VariablesScope createFromMethod(VariablesScope classSignature, Method method) {
        final Map<IdSymbol, ExpressionType> types = new HashMap<>(classSignature.variableTypes);
        for (Formal formal : method.getFormals()) {
            types.put(formal.getIdentifier(), ExpressionType.create(formal.getDeclaredType()));
        }
        return new VariablesScope(types);
    }

    private VariablesScope(Map<IdSymbol, ExpressionType> variableTypes) {
        this.variableTypes = variableTypes;
    }

    public VariablesScope addVariable(IdSymbol variableId, IdSymbol type) {
        final Map<IdSymbol, ExpressionType> newTypes = new HashMap<>(variableTypes);
        newTypes.put(variableId, ExpressionType.create(type));
        return new VariablesScope(newTypes);
    }

    public boolean containsVariable(IdSymbol variableId) {
        return this.variableTypes.containsKey(variableId);
    }

    public ExpressionType getVariableType(IdSymbol variableId) {
        return this.variableTypes.get(variableId);
    }
}
