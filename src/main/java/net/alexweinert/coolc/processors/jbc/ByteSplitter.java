package net.alexweinert.coolc.processors.jbc;

class ByteSplitter {
    public byte[] splitInt(int value) {
        return new byte[] { (byte) (value >>> 24 & 0xFF), (byte) (value >>> 16 & 0xFF), (byte) (value >>> 8 & 0xFF),
                (byte) (value & 0xFF) };
    }

    public byte[] splitChar(char value) {
        return new byte[] { (byte) (value >>> 8 & 0xFF), (byte) (value & 0xFF) };
    }
}
