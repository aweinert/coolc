package net.alexweinert.coolc.representations.graph;

import java.util.HashSet;
import java.util.Set;

public class Graph<T> {
    public static class Builder<T> {
        private Set<Node<T>> nodes = new HashSet<>();
        private Set<Edge<T>> edges = new HashSet<>();

        public void addNode(T value) {
            this.nodes.add(new Node<>(value));
        }

        public void addEdge(T source, T target) {
            final Node<T> sourceNode = new Node<>(source);
            this.nodes.add(sourceNode);
            final Node<T> targetNode = new Node<>(target);
            this.nodes.add(targetNode);
            this.edges.add(new Edge<>(sourceNode, targetNode));
        }

        public Graph<T> build() {
            return new Graph<>(this.nodes, this.edges);
        }
    }

    private final Set<Node<T>> nodes;
    private final Set<Edge<T>> edges;

    public Graph(Set<Node<T>> nodes, Set<Edge<T>> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public Set<Node<T>> getNodes() {
        return nodes;
    }

    public Set<Edge<T>> getEdges() {
        return edges;
    }

}
