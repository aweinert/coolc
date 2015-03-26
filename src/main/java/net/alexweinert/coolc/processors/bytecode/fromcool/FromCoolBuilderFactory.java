package net.alexweinert.coolc.processors.bytecode.fromcool;

import net.alexweinert.coolc.processors.cool.tohighlevel.CoolBackendBuilder;
import net.alexweinert.coolc.processors.cool.tohighlevel.CoolBackendBuilderFactory;
import net.alexweinert.coolc.representations.bytecode.ByteClass;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

public class FromCoolBuilderFactory implements CoolBackendBuilderFactory<ByteClass> {

    @Override
    public CoolBackendBuilder<ByteClass> createBuilder(IdSymbol classIdSymbol) {
        return new FromCoolBuilder();
    }

}
