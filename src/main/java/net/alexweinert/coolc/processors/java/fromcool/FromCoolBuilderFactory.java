package net.alexweinert.coolc.processors.java.fromcool;

import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

public abstract class FromCoolBuilderFactory {
    public static FromCoolBuilderFactory createJavaBuilderFactory() {
        return new FromCoolBuilderFactory() {
            @Override
            public FromCoolBuilder createBuilder(final IdSymbol classIdSymbol) {
                return new JavaClassBuilder(classIdSymbol);
            }
        };
    }

    public abstract FromCoolBuilder createBuilder(final IdSymbol classIdSymbol);
}
