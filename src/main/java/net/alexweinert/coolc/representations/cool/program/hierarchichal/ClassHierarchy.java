package net.alexweinert.coolc.representations.cool.program.hierarchichal;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.representations.cool.program.parsed.Program;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

public class ClassHierarchy {

    final private Map<IdSymbol, List<IdSymbol>> parentClasses;

    /**
     * The given program may not contain circular inheritance, redefinitions of base classes, or classes that inherit
     * from base classes
     */
    public static ClassHierarchy create(Program program) {
        return ClassHierarchyFactory.buildHierarchy(program);
    }

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
        final List<IdSymbol> commonParents = new LinkedList<>(this.parentClasses.get(classOne));
        commonParents.retainAll(this.parentClasses.get(classTwo));

        return commonParents.get(0);
    }

    /**
     * Returns all ancestors of the given class, including the class itself
     */
    public List<IdSymbol> getWeakAncestors(IdSymbol classIdentifier) {
        return new LinkedList<>(this.parentClasses.get(classIdentifier));
    }

    /**
     * Returns all ancestors of the given class, excluding the class itself
     */
    public List<IdSymbol> getStrictAncestors(IdSymbol classIdentifier) {
        final List<IdSymbol> weakAncestors = this.getWeakAncestors(classIdentifier);
        weakAncestors.remove(0);
        return weakAncestors;
    }
}
