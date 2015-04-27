package net.alexweinert.coolc.representations.jbc.instructions;

import net.alexweinert.coolc.processors.jbc.JbcEncoder;
import net.alexweinert.coolc.processors.jbc.JbcEncoding;

public class PushShortInstruction extends OpCode {

    private final int value;

    PushShortInstruction(int value) {
        this.value = value;
    }

    @Override
    public void encode(JbcEncoder encoder) {
        assert this.value < Short.MAX_VALUE;
        encoder.encodeSiPush((short) this.value);
    }

    @Override
    public int getLength(JbcEncoding encoding) {
        return encoding.getSiPushLength();
    }

}
