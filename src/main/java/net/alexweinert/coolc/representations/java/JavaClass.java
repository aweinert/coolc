package net.alexweinert.coolc.representations.java;

public class JavaClass {
    private final String identifier;
    private final String definition;

    public JavaClass(String identifier, String definition) {
        this.identifier = identifier;
        this.definition = definition;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDefinition() {
        return definition;
    }

}
