package net.alexweinert.coolc.representations.bytecode;

public class Attribute {
    private final String id;
    private final String type;
    private final String initMethodId;

    public Attribute(String id, String type, String initMethodId) {
        this.id = id;
        this.type = type;
        this.initMethodId = initMethodId;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getInitMethodId() {
        return initMethodId;
    }
}
