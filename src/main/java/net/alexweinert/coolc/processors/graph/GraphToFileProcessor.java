package net.alexweinert.coolc.processors.graph;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.alexweinert.pipelines.Backend;
import net.alexweinert.pipelines.ProcessorException;
import net.alexweinert.coolc.representations.graph.Edge;
import net.alexweinert.coolc.representations.graph.Graph;
import net.alexweinert.coolc.representations.graph.Node;

public class GraphToFileProcessor<T> implements Backend<Collection<Graph<T>>> {

    private final Path pathToFolder;

    public GraphToFileProcessor(Path pathToFolder) {
        this.pathToFolder = pathToFolder;
    }

    @Override
    public void process(Collection<Graph<T>> input) throws ProcessorException {
        try {
            Integer nodeIndex = 0;
            final Map<Node<T>, Integer> indices = new HashMap<>();
            for (Graph<T> graph : input) {
                final Writer writer = new FileWriter(this.pathToFolder.resolve(graph.getIdentifier() + ".dot")
                        .toString());
                writer.write("digraph method {\n");
                for (Node<T> node : graph.getNodes()) {
                    indices.put(node, nodeIndex);
                    writer.write(nodeIndex + " [label=\"" + String.valueOf(node.getValue()) + "\"];\n");
                    nodeIndex += 1;
                }

                for (Edge<T> edge : graph.getEdges()) {
                    writer.write(indices.get(edge.getSource()) + " -> " + indices.get(edge.getTarget()) + ";\n");
                }
                writer.write("}\n");
                writer.close();
            }
        } catch (IOException e) {
            throw new ProcessorException(e);
        }

    }

}
