package net.alexweinert.coolc.program.information;

import java.util.Map;

import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.symboltables.IdSymbol;

class ClassSignature {

    private final Map<IdSymbol, Attribute> attributes;
    private final Map<IdSymbol, MethodSignature> methods;

    public ClassSignature(Map<IdSymbol, Attribute> attributes, Map<IdSymbol, MethodSignature> methods) {
        this.attributes = attributes;
        this.methods = methods;
    }

}