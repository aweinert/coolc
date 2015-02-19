package net.alexweinert.coolc.program.information;

import java.util.HashMap;
import java.util.Map;

import net.alexweinert.coolc.program.ast.Attribute;
import net.alexweinert.coolc.program.symboltables.IdSymbol;

public class DefinedClassSignature extends ClassSignature {

    public static DefinedClassSignature create(IdSymbol classId, ClassHierarchy hierarchy,
            Map<IdSymbol, DeclaredClassSignature> declaredSignatures) {
        final Map<IdSymbol, Attribute> attributes = new HashMap<>();
        final Map<IdSymbol, MethodSignature> methods = new HashMap<>();

        for (IdSymbol ancestor : hierarchy.getAncestors(classId)) {
            final DeclaredClassSignature ancestorSignature = declaredSignatures.get(ancestor);
            for (Attribute attribute : ancestorSignature.getAttributes()) {
                attributes.put(attribute.getName(), attribute);
            }
            for (MethodSignature method : ancestorSignature.getMethodSignatures()) {
                methods.put(method.getIdentifier(), method);
            }
        }

        return new DefinedClassSignature(attributes, methods);
    }

    private DefinedClassSignature(final Map<IdSymbol, Attribute> attributes,
            final Map<IdSymbol, MethodSignature> methods) {
        super(attributes, methods);
    }
}
