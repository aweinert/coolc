package net.alexweinert.coolc.processors.bytecode.fromcool;

import java.util.List;

import net.alexweinert.coolc.processors.cool.tohighlevel.CoolBackendBuilder;
import net.alexweinert.coolc.processors.cool.tohighlevel.CoolBackendBuilderFactory;
import net.alexweinert.coolc.representations.bytecode.ByteClass;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

public class FromCoolBuilderFactory implements CoolBackendBuilderFactory<ByteClass, List<ByteClass>> {

    @Override
    public CoolBackendBuilder<ByteClass, List<ByteClass>> createBuilder(IdSymbol classIdSymbol) {
        return new FromCoolBuilder();
    }

}
