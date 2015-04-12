package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

public abstract class OpCode {
    public static OpCode buildNop() {
        return new Nop();
    }

    public abstract void encode(JbcEncoder encoder);

    public abstract int getLength(JbcEncoding encoding);
}
