package net.alexweinert.coolc.semantic_check;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.program.symboltables.AbstractSymbol;

class ClassHierarchyBuilder {
    private class Pair<T, U> {
        final private T x;
        final private U y;

        public Pair(T x, U y) {
            this.x = x;
            this.y = y;
        }
    }

    final private Collection<Pair<AbstractSymbol, AbstractSymbol>> inheritanceInfo = new HashSet<>();

    void addInheritance(AbstractSymbol childClass, AbstractSymbol parentClass) {
        this.inheritanceInfo.add(new Pair<>(childClass, parentClass));
    }

    ClassHierarchy buildHierarchy() {
        final Map<AbstractSymbol, List<AbstractSymbol>> hierarchy = new HashMap<>();
        for (Pair<AbstractSymbol, AbstractSymbol> singleInheritance : this.inheritanceInfo) {
            final List<AbstractSymbol> ancestors = this.computeAncestors(singleInheritance.x);
            hierarchy.put(singleInheritance.x, ancestors);
        }
        return new ClassHierarchy(hierarchy);
    }

    private List<AbstractSymbol> computeAncestors(AbstractSymbol root) {
        final List<AbstractSymbol> ancestors = new LinkedList<>();
        ancestors.add(root);
        Pair<AbstractSymbol, AbstractSymbol> singleInheritance = this.findSingleInheritance(root);
        while (singleInheritance != null) {
            ancestors.add(singleInheritance.y);
            singleInheritance = this.findSingleInheritance(singleInheritance.y);
        }
        return ancestors;
    }

    private Pair<AbstractSymbol, AbstractSymbol> findSingleInheritance(AbstractSymbol lhs) {
        for (Pair<AbstractSymbol, AbstractSymbol> singleInheritance : this.inheritanceInfo) {
            if (singleInheritance.x.equals(lhs)) {
                return singleInheritance;
            }
        }
        return null;
    }
}
