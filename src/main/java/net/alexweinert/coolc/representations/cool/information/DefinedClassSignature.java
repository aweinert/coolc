package net.alexweinert.coolc.representations.cool.information;

import java.util.HashMap;
import java.util.Map;

import net.alexweinert.coolc.representations.cool.ast.Attribute;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

public class DefinedClassSignature extends ClassSignature {

    /**
     * The given signatures together with the hierarchy must obey the Cool-rules for overriding, i.e., subclasses are
     * not allowed to override attributes and they may only override methods if the signature of the method is exactly
     * the same.
     */
    public static DefinedClassSignature create(IdSymbol classId, ClassHierarchy hierarchy,
            Map<IdSymbol, DeclaredClassSignature> declaredSignatures) {
        final Map<IdSymbol, Attribute> attributes = new HashMap<>();
        final Map<IdSymbol, MethodSignature> methods = new HashMap<>();

        /* When collecting the signatures, we only collect the signatures of the most specific parentclass. This allows
         * us to handle the types of programs that come out of the removal of the self type */
        for (IdSymbol ancestor : hierarchy.getWeakAncestors(classId)) {
            final DeclaredClassSignature ancestorSignature = declaredSignatures.get(ancestor);
            for (Attribute attribute : ancestorSignature.getAttributes()) {
                if (!attributes.containsKey(attribute.getName())) {
                    attributes.put(attribute.getName(), attribute);
                }
            }
            for (MethodSignature method : ancestorSignature.getMethodSignatures()) {
                if (!methods.containsKey(method.getIdentifier())) {
                    methods.put(method.getIdentifier(), method);
                }
            }
        }

        return new DefinedClassSignature(attributes, methods);
    }

    private DefinedClassSignature(final Map<IdSymbol, Attribute> attributes,
            final Map<IdSymbol, MethodSignature> methods) {
        super(attributes, methods);
    }
}
