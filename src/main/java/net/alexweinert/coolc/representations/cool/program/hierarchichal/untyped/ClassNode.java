package net.alexweinert.coolc.representations.cool.program.hierarchichal.untyped;

import net.alexweinert.coolc.representations.cool.program.hierarchichal.DefinedClassSignature;
import net.alexweinert.coolc.representations.cool.program.parsed.Features;
import net.alexweinert.coolc.representations.cool.symboltables.IdSymbol;

public class ClassNode extends net.alexweinert.coolc.representations.cool.program.parsed.ClassNode {
    private final DefinedClassSignature definedSignature;

    public ClassNode(String filename, int lineNumber, IdSymbol a1, IdSymbol a2, Features a3,
            DefinedClassSignature signature) {
        super(filename, lineNumber, a1, a2, a3);
        this.definedSignature = signature;
    }

    public DefinedClassSignature getDefinedSignature() {
        return definedSignature;
    }

}
