package com.noodlegamer76.grimoires.spellcrafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.noodlegamer76.grimoires.spellcrafting.graph.Graph;
import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.Node;
import imgui.ImVec2;
import imgui.extension.imnodes.ImNodes;

import java.util.HashMap;
import java.util.Map;

public class Spell {
    private final Graph graph = new Graph();
    private final String name;
    private final Map<Integer, ImVec2> nodePositions = new HashMap<>();

    public Spell(String name) {
        this.name = name;
    }

    public Graph getGraph() {
        return graph;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, ImVec2> getNodePositions() {
        return nodePositions;
    }

    public void addPosition(Integer id, ImVec2 position) {
        nodePositions.put(id, position);
    }

    public void addPositions() {
        nodePositions.clear();
        for (Node node: graph.getNodes()) {
            ImVec2 position = new ImVec2();
            ImNodes.getNodeEditorSpacePos(node.getId(), position);

            nodePositions.put(node.getId(), position);
        }
    }

    public JsonObject saveSpell() {
        JsonObject spell = new JsonObject();
        spell.add("g", getGraph().save());
        spell.add("p", savePositions());
        return spell;
    }

    public JsonObject savePositions() {
        JsonObject positions = new JsonObject();
        for (Map.Entry<Integer, ImVec2> entry: nodePositions.entrySet()) {
            JsonObject position = new JsonObject();
            position.addProperty("x", entry.getValue().x);
            position.addProperty("y", entry.getValue().y);
            positions.add(String.valueOf(entry.getKey()), position);
        }
        return positions;
    }

    public void loadSpell(JsonObject spell) {
        graph.load(spell.getAsJsonObject("g"));
        loadPositions(spell.getAsJsonObject("p"));
    }

    public void loadPositions(JsonObject positions) {
        for (Map.Entry<String, JsonElement> element: positions.entrySet()) {
            JsonObject position = element.getValue().getAsJsonObject();
            int id = Integer.parseInt(element.getKey());
            ImVec2 positionVec = new ImVec2(position.get("x").getAsFloat(), position.get("y").getAsFloat());
            nodePositions.put(id, positionVec);
        }
    }
}