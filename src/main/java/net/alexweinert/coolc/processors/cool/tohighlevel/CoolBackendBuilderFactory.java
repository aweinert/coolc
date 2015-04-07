package net.alexweinert.coolc.processors.cool.tohighlevel;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

public interface CoolBackendBuilderFactory<T, U> {
    /**
     * @param classIdSymbol
     *            The id of the class to be constructed. If null, this object is only going to be used to construct
     *            basic classes.
     * @return A builder for the representation T
     */
    CoolBackendBuilder<T, U> createBuilder(final IdSymbol classIdSymbol);
}
