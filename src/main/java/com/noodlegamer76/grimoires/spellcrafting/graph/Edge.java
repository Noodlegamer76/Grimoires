package com.noodlegamer76.grimoires.spellcrafting.graph;

import com.google.gson.JsonObject;
import com.noodlegamer76.grimoires.imgui.pins.NodePin;

import java.util.Objects;

public record Edge(NodePin<?> from, NodePin<?> to, int id) {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge other) {
            return from().equals(other.from()) && to().equals(other.to());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from(), to());
    }

    @Override
    public String toString() {
        return "Connection: " +
                "{From: " + from().toString() + "}, " +
                "{To " + to().toString() + "}";
    }

    public JsonObject save() {
        JsonObject edge = new JsonObject();
        edge.addProperty("i", id());
        edge.addProperty("f", from().getId());
        edge.addProperty("t", to().getId());
        return edge;
    }

    public static void load(JsonObject json, Graph graph) {
        int id = json.get("i").getAsInt();
        int fromId = json.get("f").getAsInt();
        int toId = json.get("t").getAsInt();
        NodePin<?> from = graph.getNodePinById(fromId);
        NodePin<?> to = graph.getNodePinById(toId);
        graph.addEdge(from, to, id);
    }
}