package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

public abstract class MethodEntry {

    public void encode(JbcEncoder jbcEncoder) {
        jbcEncoder.encodeMethod(this);

    }
}
