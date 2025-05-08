package com.noodlegamer76.grimoires.spellcrafting.graph.nodes;

import com.google.gson.JsonObject;
import com.noodlegamer76.grimoires.imgui.pins.NodePin;
import com.noodlegamer76.grimoires.spellcrafting.graph.Graph;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.extension.imnodes.ImNodes;

import java.util.ArrayList;

public abstract class Node {
    private int id;
    private final ArrayList<NodePin<?>> pins = new ArrayList<>();
    private String name = "Node";
    private boolean justDropped = false;

    public Node(int id, String name) {
        this.id = id;
        this.name = name;
        init();
    }

    public Node(int id) {
        this.id = id;
        init();
    }

    //for deserialization
    public Node(String name) {
        this.name = name;
    }

    public void setSize(ImVec2 size) {
        ImGui.getItemRectSize().set(size);
    }

    public abstract void init();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node other) {
            return getId() == other.getId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Node ID: " + id;
    }

    public void render() {
        beginRender();
        renderNode();
        renderPins();
        endRender();
    }

    public void beginRender() {
        if (isJustDropped()) {
            ImNodes.setNodeScreenSpacePos(getId(), ImGui.getMousePosX(), ImGui.getMousePosY());
            setJustDropped(false);
        }

        ImNodes.beginNode(getId());
    }

    public void endRender() {
        ImNodes.endNode();
    }

    public void renderNode() {
        ImNodes.beginNodeTitleBar();
        ImGui.text(getName());
        ImNodes.endNodeTitleBar();
    }

    public void renderPins() {
        for (NodePin<?> pin: pins) {
            if (pin.getIOType() == NodePin.IOType.INPUT) {
                ImNodes.beginInputAttribute(pin.getId(), pin.getShape());
                ImGui.text(pin.getName());
                ImNodes.endInputAttribute();
            }
            else {
                ImNodes.beginOutputAttribute(pin.getId(), pin.getShape());
                ImGui.text(pin.getName());
                ImNodes.endOutputAttribute();
            }
        }
    }

    public void addPin(NodePin<?> attribute) {
        pins.add(attribute);
    }

    public ArrayList<NodePin<?>> getPins() {
        return pins;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isJustDropped() {
        return justDropped;
    }

    public void setJustDropped(boolean justDropped) {
        this.justDropped = justDropped;
    }

    public JsonObject save() {
        JsonObject node = new JsonObject();
        node.addProperty("t", getClass().getSimpleName());
        node.addProperty("y", getId());
        JsonObject pins = new JsonObject();
        for (NodePin<?> pin: getPins()) {
            pins.add(String.valueOf(pin.getId()), pin.save());
        }
        node.add("p", pins);
        return node;
    }

    public static Node load(JsonObject json, Graph graph) {
        String type = json.get("t").getAsString();
        int id = json.get("y").getAsInt();
        Node node;

        node = NodeLoader.load(type);

        if (node != null) {
            node.setId(id);
            JsonObject pins = json.getAsJsonObject("p");
            pins.entrySet().forEach(entry -> NodePin.load(entry.getValue().getAsJsonObject(), node));
        }

        return node;
    }
}
