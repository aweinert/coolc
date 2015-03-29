package net.alexweinert.coolc.representations.graph;

public class Node<T> {
    final private T value;

    Node(T value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        Node other = (Node) obj;
        return this.value == other.value;
    }

    public T getValue() {
        return value;
    }
}
