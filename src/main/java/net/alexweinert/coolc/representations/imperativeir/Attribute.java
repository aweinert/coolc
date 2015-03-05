package net.alexweinert.coolc.representations.imperativeir;

public class Attribute {
    private final String identifier;

    public static Attribute create(String identifier) {
        return new Attribute(identifier);
    }

    private Attribute(String identifier) {
        this.identifier = identifier;
    }
}
