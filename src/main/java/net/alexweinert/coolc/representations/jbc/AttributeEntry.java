package net.alexweinert.coolc.representations.jbc;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;

public abstract class AttributeEntry {

    public abstract void encode(JbcEncoder encoder);
}
