package net.alexweinert.coolc.processors.java.fromcool;

import net.alexweinert.coolc.processors.cool.tohighlevel.CoolBackendBuilderFactory;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;
import net.alexweinert.coolc.representations.java.JavaClass;
import net.alexweinert.coolc.representations.java.JavaProgram;

public class JavaClassBuilderFactory implements CoolBackendBuilderFactory<JavaClass, JavaProgram> {
    @Override
    public JavaClassBuilder createBuilder(final IdSymbol classIdSymbol) {
        return new JavaClassBuilder(classIdSymbol);
    }

}
