package com.noodlegamer76.grimoires.spellcrafting.graph.nodes;

import com.google.gson.JsonObject;
import com.noodlegamer76.grimoires.imgui.pins.NodePin;
import com.noodlegamer76.grimoires.spellcrafting.graph.Edge;
import com.noodlegamer76.grimoires.spellcrafting.graph.Graph;
import com.noodlegamer76.grimoires.spellcrafting.gui.SpellEditorRenderer;
import com.noodlegamer76.grimoires.spellcrafting.simulation.ManaParticle;
import com.noodlegamer76.grimoires.spellcrafting.simulation.SpellSimulation;
import imgui.ImGui;
import imgui.extension.imnodes.ImNodes;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    private final ArrayList<NodePin<?>> pins = new ArrayList<>();
    private int id;
    private String name;
    private boolean justDropped = false;
    private boolean isInput = false;
    private int pinVariant;
    private final List<ManaParticle> manaParticles = new ArrayList<>();
    private final List<ManaParticle> manaParticlesOutput = new ArrayList<>();
    private int maxParticles;
    protected static final int MAX_NODE_PARTICLES = 10000;
    protected NodeHolder<?> nodeHolder;

    public Node(String name, int maxParticles) {
        this.name = name;
        this.maxParticles = maxParticles;
    }

    public abstract boolean canProcess(SpellSimulation simulation);

    public abstract void process(SpellSimulation simulation);

    public void initForSpellEditor(int variant) {
        Graph graph = SpellEditorRenderer.getInstance().getSpell().getGraph();
        pins.clear();
        List<Edge> edges = new ArrayList<>(graph.getEdgesFrom(this));
        edges.forEach(graph::removeEdge);
        nodeHolder = NodeRegistryUtils.getNodeHolder(getClass());

        NodeRegistryUtils.getPinsFromNodeClass(getClass(), variant).forEach(pin -> {
            pins.add(pin);
            pin.setId(SpellEditorRenderer.getInstance().getSpell().getGraph().nextId());
            pin.setParentNode(this);
        });
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInput(boolean input) {
        isInput = input;
    }

    public boolean isInput() {
        return isInput;
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
        attribute.setParentNode(this);
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

    public int getPinVariant() {
        return pinVariant;
    }

    public int getMaxParticles() {
        return maxParticles;
    }

    public int getTotalParticles() {
        return manaParticles.size() + manaParticlesOutput.size();
    }

    public void setMaxParticles(int maxParticles) {
        this.maxParticles = maxParticles;
    }

    //for spell editor use
    public void setPinVariant(int pinVariant) {
        this.pinVariant = pinVariant;
        initForSpellEditor(pinVariant);
    }

    public JsonObject save() {
        JsonObject node = new JsonObject();
        node.addProperty("v", getPinVariant());
        node.addProperty("i", isInput);
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
        int variant = json.get("v").getAsInt();
        boolean isInput = json.get("i").getAsBoolean();
        String type = json.get("t").getAsString();
        int id = json.get("y").getAsInt();
        Node node;

        node = NodeRegistryUtils.getNode(type);

        if (node != null) {
            node.setId(id);
            JsonObject pins = json.getAsJsonObject("p");
            pins.entrySet().forEach(entry -> NodePin.load(entry.getValue().getAsJsonObject(), node));
            node.setInput(isInput);
            node.setPinVariant(variant);
        }

        return node;
    }

    public List<ManaParticle> getManaParticles() {
        return manaParticles;
    }

    public List<ManaParticle> getManaParticlesOutput() {
        return manaParticlesOutput;
    }

    public void addManaParticle(ManaParticle manaParticles) {
        this.manaParticles.add(manaParticles);
    }

    public void addManaParticles(List<ManaParticle> manaParticles) {
        this.manaParticles.addAll(manaParticles);
    }

    public void addManaParticlesOutput(ManaParticle manaParticles) {
        this.manaParticlesOutput.add(manaParticles);
    }

    public void addManaParticlesOutput(List<ManaParticle> manaParticles) {
        this.manaParticlesOutput.addAll(manaParticles);
    }

    public boolean hasManaParticles() {
        return !manaParticles.isEmpty();
    }
}
