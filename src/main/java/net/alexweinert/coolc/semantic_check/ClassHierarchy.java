package net.alexweinert.coolc.semantic_check;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.program.symboltables.IdSymbol;

public class ClassHierarchy {

    final private Map<IdSymbol, List<IdSymbol>> parentClasses;

    /**
     * @param parentClasses
     *            Contains a mapping of classes to all classes that they conform to, in order. The list must contain the
     *            class itself.
     */
    ClassHierarchy(Map<IdSymbol, List<IdSymbol>> parentClasses) {
        this.parentClasses = new HashMap<>(parentClasses);
    }

    public boolean conformsTo(IdSymbol classOne, IdSymbol classTwo) {
        return this.parentClasses.get(classOne).contains(classTwo);
    }

    public IdSymbol getLeastUpperBound(IdSymbol classOne, IdSymbol classTwo) {
        final List<IdSymbol> commonParents = this.parentClasses.get(classOne);
        commonParents.retainAll(this.parentClasses.get(classTwo));

        return commonParents.get(0);
    }

    public List<IdSymbol> getAncestors(IdSymbol classIdentifier) {
        return new LinkedList<>(this.parentClasses.get(classIdentifier));
    }
}
