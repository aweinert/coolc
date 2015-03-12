package net.alexweinert.coolc.representations.cool.program.hierarchichal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import net.alexweinert.coolc.representations.cool.program.parsed.Attribute;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

public class ClassSignature {
    private final Map<IdSymbol, Attribute> attributes;
    private final Map<IdSymbol, MethodSignature> methods;

    public ClassSignature(Map<IdSymbol, Attribute> attributes, Map<IdSymbol, MethodSignature> methods) {
        this.attributes = attributes;
        this.methods = methods;
    }

    public Collection<Attribute> getAttributes() {
        return new HashSet<>(this.attributes.values());
    }

    public Collection<MethodSignature> getMethodSignatures() {
        return new HashSet<>(this.methods.values());
    }

    public Attribute getAttribute(IdSymbol id) {
        return this.attributes.get(id);
    }

    public MethodSignature getMethodSignature(IdSymbol id) {
        return this.methods.get(id);
    }
}