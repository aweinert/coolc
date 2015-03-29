package net.alexweinert.coolc.representations.graph;

public class Edge<T> {
    private final Node<T> source;
    private final Node<T> target;

    public Edge(Node<T> source, Node<T> target) {
        this.source = source;
        this.target = target;
    }

    public Node<T> getSource() {
        return source;
    }

    public Node<T> getTarget() {
        return target;
    }

}
