package net.alexweinert.coolc.representations.jbc;

import java.nio.charset.Charset;

public class Utf8Constant extends ConstantPoolEntry {
    private final String value;

    Utf8Constant(String value) {
        super((byte) 0x01);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public byte[] toBytes() {
        final byte[] valueArray = Charset.forName("UTF-8").encode(this.value).array();
        byte[] returnValue = new byte[valueArray.length + 3];
        returnValue[0] = 0x01;
        returnValue[1] = (byte) (valueArray.length >> 8 & 0xFF);
        returnValue[2] = (byte) (valueArray.length & 0xFF);
        System.arraycopy(valueArray, 0, returnValue, 3, valueArray.length);
        return returnValue;
    }

}
