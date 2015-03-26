package net.alexweinert.coolc.representations.bytecode;

public class TypedId {
    private final String type;
    private final String id;

    public TypedId(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
