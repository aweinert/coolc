package net.alexweinert.coolc.processors.cool.tohighlevel;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

public interface CoolBackendBuilderFactory<T> {
    /**
     * @param classIdSymbol
     *            The id of the class to be constructed. If null, this object is only going to be used to construct
     *            basic classes.
     * @return A builder for the representation T
     */
    CoolBackendBuilder<T> createBuilder(final IdSymbol classIdSymbol);
}
