package net.alexweinert.coolc.representations.bytecode;

import java.util.LinkedList;
import java.util.List;

public class ByteClass {

    public static class Builder {
        private final String id;
        private final String parent;
        private final List<Attribute> attributes = new LinkedList<>();
        private final List<Method> methods = new LinkedList<>();

        public Builder(String id, String parent) {
            this.id = id;
            this.parent = parent;
        }

        public Builder addAttribute(Attribute attribute) {
            this.attributes.add(attribute);
            return this;
        }

        public Builder addMethod(Method method) {
            this.methods.add(method);
            return this;
        }

        public ByteClass build() {
            return new ByteClass(id, parent, attributes, methods);
        }

    }

    private final String id;
    private final String parent;
    private final List<Attribute> attributes;
    private final List<Method> methods;

    public ByteClass(String id, String parent, List<Attribute> attributes, List<Method> methods) {
        this.id = id;
        this.parent = parent;
        this.attributes = attributes;
        this.methods = methods;
    }

    public String getId() {
        return id;
    }

    public String getParent() {
        return parent;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public Attribute getAttribute(String id) {
        for (Attribute attr : this.attributes) {
            if (attr.getId().equals(id)) {
                return attr;
            }
        }
        return null;
    }
}
