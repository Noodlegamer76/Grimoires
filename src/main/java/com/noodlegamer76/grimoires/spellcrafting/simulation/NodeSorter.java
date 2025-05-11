package com.noodlegamer76.grimoires.spellcrafting.simulation;

import com.noodlegamer76.grimoires.spellcrafting.graph.Graph;
import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.Node;

import java.util.*;

public class NodeSorter {
    private final Graph graph;
    private final Node input;
    private final List<Node> sortedNodes = new ArrayList<>();

    public NodeSorter(Graph graph, Node input) {
        this.input = input;
        this.graph = graph;
        sort();
    }

    private void sort() {
        Map<Node, Integer> distances = new HashMap<>();
        Queue<Node> queue = new LinkedList<>();

        distances.put(input, 0);
        queue.add(input);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            int currentDistance = distances.get(current);

            for (Node neighbor : graph.getConnectedNodes(current)) {
                if (!distances.containsKey(neighbor)) {
                    distances.put(neighbor, currentDistance + 1);
                    queue.add(neighbor);
                }
            }
        }

        sortedNodes.addAll(distances.keySet());
        sortedNodes.sort(Comparator.comparingInt(distances::get));
    }

    public Node nextNode() {
        if (sortedNodes.isEmpty()) {
            return null;
        }
        Node node = sortedNodes.removeFirst();
        sortedNodes.addLast(node);
        return node;
    }

    public List<Node> getSortedNodes() {
        return sortedNodes;
    }

    public Node getInput() {
        return input;
    }
}
