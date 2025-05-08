package com.noodlegamer76.grimoires.spellcrafting.graph;

import com.google.gson.JsonObject;
import com.noodlegamer76.grimoires.imgui.pins.NodePin;
import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.Node;

import java.util.*;

public class Graph {
    private final Map<Node, List<Edge>> adjacencyList = new HashMap<>();
    private Integer idIncrement = 0;

    public void addNode(Node node, boolean setId) {
        if (setId) {
            node.setId(nextId());
        }
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    public int nextId() {
        return idIncrement++;
    }

    public Node getNodeById(int id) {
        for (Node node : adjacencyList.keySet()) {
            if (node.getId() == id) {
                return node;
            }
        }
        return null;
    }

    public NodePin<?> getNodePinById(int id) {
        for (Node node : adjacencyList.keySet()) {
            for (NodePin<?> pin : node.getPins()) {
                if (pin.getId() == id) {
                    return pin;
                }
            }
        }
        return null;
    }

    public Node getNodeByPin(NodePin<?> pin) {
        for (Node node : adjacencyList.keySet()) {
            if (node.getPins().contains(pin)) {
                return node;
            }
        }
        return null;
    }

    public List<NodePin<?>> getAllPins() {
        List<NodePin<?>> pins = new ArrayList<>();
        for (Node node : adjacencyList.keySet()) {
            pins.addAll(node.getPins());
        }
        return pins;
    }

    public Edge getEdgeById(int id) {
        for (List<Edge> edges : adjacencyList.values()) {
            for (Edge edge : edges) {
                if (edge.id() == id) {
                    return edge;
                }
            }
        }

        return null;
    }

    public void addEdge(NodePin<?> from, NodePin<?> to) {
        Edge edge = new Edge(from, to, nextId());
        Node fromNode = getNodeByPin(from);
        Node toNode = getNodeByPin(to);

        if (fromNode != null && toNode != null) {
            adjacencyList.get(fromNode).add(edge);
            adjacencyList.get(toNode).add(edge);
        }
    }

    public void addEdge(NodePin<?> from, NodePin<?> to, int id) {
        Edge edge = new Edge(from, to, id);
        Node fromNode = getNodeByPin(from);
        Node toNode = getNodeByPin(to);

        if (fromNode != null && toNode != null) {
            adjacencyList.get(fromNode).add(edge);
            adjacencyList.get(toNode).add(edge);
        }
    }

    public void removeNode(Node node) {
        adjacencyList.remove(node);
    }

    public List<Edge> getEdgesFrom(Node node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    public List<Edge> getOutgoingEdgesFrom(Node node) {
        List<Edge> outgoing = new ArrayList<>();
        List<Edge> allEdges = getEdgesFrom(node);

        for (Edge edge: allEdges) {
            if (edge.from().equals(node)) {
                outgoing.add(edge);
            }
        }

        return outgoing;
    }

    public List<Edge> getIncomingEdgesFrom(Node node) {
        List<Edge> incoming = new ArrayList<>();
        List<Edge> allEdges = getEdgesFrom(node);

        for (Edge edge: allEdges) {
            if (edge.to().equals(node)) {
                incoming.add(edge);
            }
        }

        return incoming;
    }

    public Set<Node> getNodes() {
        return adjacencyList.keySet();
    }

    public List<Edge> getAllEdges() {
        List<Edge> edges = new ArrayList<>();
        for (List<Edge> edgeList : adjacencyList.values()) {
            for (Edge edge : edgeList) {
                if (!edges.contains(edge)) {
                    edges.add(edge);
                }
            }
        }
        return edges;
    }

    public void removeEdge(Edge edge) {
        adjacencyList.values().forEach(edges -> edges.remove(edge));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node node : adjacencyList.keySet()) {
            sb.append(node).append(": ").append(adjacencyList.get(node)).append("\n");
        }
        return sb.toString();
    }

    public Map<Node, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public void setIdIncrement(Integer idIncrement) {
        this.idIncrement = idIncrement;
    }

    public JsonObject save() {
        JsonObject json = new JsonObject();
        json.addProperty("i", idIncrement);
        JsonObject nodes = new JsonObject();
        for (Node node : adjacencyList.keySet()) {
            nodes.add(String.valueOf(node.getId()), node.save());
        }
        json.add("n", nodes);
        JsonObject edges = new JsonObject();
        for (Edge edge : getAllEdges()) {
            edges.add(String.valueOf(edge.id()), edge.save());
        }
        json.add("e", edges);
        return json;
    }

    public void load(JsonObject json) {
        idIncrement = json.get("i").getAsInt();

        JsonObject nodes = json.getAsJsonObject("n");
        nodes.entrySet().forEach(entry -> {
            Node node = Node.load(entry.getValue().getAsJsonObject(), this);
            if (node != null) {
                addNode(node, false);
            }
            else {
                System.out.println("Failed to load node: " + entry.getValue().getAsJsonObject().get("t").getAsString());
            }
        });
        JsonObject edges = json.getAsJsonObject("e");
        edges.entrySet().forEach(entry -> {
            Edge.load(entry.getValue().getAsJsonObject(), this);
        });
    }
}
