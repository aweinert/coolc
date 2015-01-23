package net.alexweinert.coolc.semantic_check;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.alexweinert.coolc.program.symboltables.AbstractSymbol;

public class ClassHierarchy {

    final private Map<AbstractSymbol, List<AbstractSymbol>> parentClasses;

    /**
     * @param parentClasses
     *            Contains a mapping of classes to all classes that they conform to, in order. The list must contain the
     *            class itself.
     */
    ClassHierarchy(Map<AbstractSymbol, List<AbstractSymbol>> parentClasses) {
        this.parentClasses = new HashMap<>(parentClasses);
    }

    public boolean conformsTo(AbstractSymbol classOne, AbstractSymbol classTwo) {
        return this.parentClasses.get(classOne).contains(classTwo);

    }

    public AbstractSymbol getLeastUpperBound(AbstractSymbol classOne, AbstractSymbol classTwo) {
        final List<AbstractSymbol> commonParents = this.parentClasses.get(classOne);
        commonParents.retainAll(this.parentClasses.get(classTwo));

        return commonParents.get(0);
    }
}
