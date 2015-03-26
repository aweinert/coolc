package net.alexweinert.coolc.representations.bytecode;

public class Attribute {
    private final String type;
    private final String id;
    private final String initMethodId;

    public Attribute(String type, String id, String initMethodId) {
        this.type = type;
        this.id = id;
        this.initMethodId = initMethodId;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getInitMethodId() {
        return initMethodId;
    }
}
