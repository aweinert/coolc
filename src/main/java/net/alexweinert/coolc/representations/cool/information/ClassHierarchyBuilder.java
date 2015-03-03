package net.alexweinert.coolc.representations.cool.information;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.cool.symboltables.IdTable;

class ClassHierarchyBuilder {
    private class Pair<T, U> {
        final private T x;
        final private U y;

        public Pair(T x, U y) {
            this.x = x;
            this.y = y;
        }
    }

    final private Collection<Pair<IdSymbol, IdSymbol>> inheritanceInfo = new HashSet<>();

    void addInheritance(IdSymbol childClass, IdSymbol parentClass) {
        this.inheritanceInfo.add(new Pair<>(childClass, parentClass));
    }

    ClassHierarchy buildHierarchy() {
        final Map<IdSymbol, List<IdSymbol>> hierarchy = new HashMap<>();
        hierarchy.put(IdTable.getInstance().getObjectSymbol(),
                Collections.singletonList(IdTable.getInstance().getObjectSymbol()));
        for (Pair<IdSymbol, IdSymbol> singleInheritance : this.inheritanceInfo) {
            final List<IdSymbol> ancestors = this.computeAncestors(singleInheritance.x);
            hierarchy.put(singleInheritance.x, ancestors);
        }
        return new ClassHierarchy(hierarchy);
    }

    private List<IdSymbol> computeAncestors(IdSymbol root) {
        final List<IdSymbol> ancestors = new LinkedList<>();
        ancestors.add(root);
        Pair<IdSymbol, IdSymbol> singleInheritance = this.findSingleInheritance(root);
        while (singleInheritance != null) {
            ancestors.add(singleInheritance.y);
            singleInheritance = this.findSingleInheritance(singleInheritance.y);
        }
        return ancestors;
    }

    private Pair<IdSymbol, IdSymbol> findSingleInheritance(IdSymbol lhs) {
        for (Pair<IdSymbol, IdSymbol> singleInheritance : this.inheritanceInfo) {
            if (singleInheritance.x.equals(lhs)) {
                return singleInheritance;
            }
        }
        return null;
    }
}
