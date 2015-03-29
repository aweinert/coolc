package net.alexweinert.coolc.representations.graph;

import java.util.HashSet;
import java.util.Set;

public class Graph<T> {
    public static class Builder<T> {

        private final String identifier;
        private Set<Node<T>> nodes = new HashSet<>();
        private Set<Edge<T>> edges = new HashSet<>();

        public Builder(String identifier) {
            this.identifier = identifier;
        }

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
            return new Graph<>(this.identifier, this.nodes, this.edges);
        }
    }

    private final String identifier;
    private final Set<Node<T>> nodes;
    private final Set<Edge<T>> edges;

    public Graph(String identifier, Set<Node<T>> nodes, Set<Edge<T>> edges) {
        this.identifier = identifier;
        this.nodes = nodes;
        this.edges = edges;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Set<Node<T>> getNodes() {
        return nodes;
    }

    public Set<Edge<T>> getEdges() {
        return edges;
    }

}
