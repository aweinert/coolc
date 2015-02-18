package net.alexweinert.coolc.semantic_check;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.program.information.ClassHierarchy;
import net.alexweinert.coolc.program.information.MethodSignature;
import net.alexweinert.coolc.program.symboltables.IdSymbol;

class ClassInterfaceBuilder {
    private Map<IdSymbol, Collection<MethodSignature>> definedSignatures = new HashMap<>();

    public void addMethodSignature(IdSymbol classIdentifier, MethodSignature signature) {
        if (!this.definedSignatures.containsKey(classIdentifier)) {
            this.definedSignatures.put(classIdentifier, new HashSet<MethodSignature>());
        }
        this.definedSignatures.get(classIdentifier).add(signature);
    }

    public Collection<ClassInterface> buildInterfaces(ClassHierarchy hierarchy) {
        final Collection<ClassInterface> returnValue = new HashSet<>();
        for (IdSymbol classIdentifier : this.definedSignatures.keySet()) {
            returnValue.add(this.buildInterfaces(classIdentifier, hierarchy));
        }
        return returnValue;
    }

    private ClassInterface buildInterfaces(IdSymbol classIdentifier, ClassHierarchy hierarchy) {
        final List<MethodSignature> methods = new LinkedList<>();
        final List<IdSymbol> ancestors = hierarchy.getAncestors(classIdentifier);
        for (IdSymbol ancestor : ancestors) {
            for (MethodSignature signature : this.definedSignatures.get(ancestor)) {
                if (ancestor.equals(classIdentifier) && this.containsEqualMethod(methods, signature)) {
                    System.out.println("ERROR: Class " + classIdentifier + " contains multiple definitions of "
                            + signature.getIdentifier());
                } else if (this.containsConflictingMethod(methods, signature)) {
                    if (ancestor.equals(classIdentifier)) {
                        System.out.println("ERROR: Class " + "");
                    }
                } else {
                    methods.add(signature);
                }
            }
        }
        return new ClassInterface(classIdentifier, methods);
    }

    private boolean containsEqualMethod(List<MethodSignature> methods, MethodSignature signature) {
        for (MethodSignature existingSignature : methods) {
            if (existingSignature.getIdentifier().equals(signature.getIdentifier())) {
                return true;
            }
        }
        return false;
    }

    private boolean containsConflictingMethod(List<MethodSignature> methods, MethodSignature signature) {
        return false;
    }
}
