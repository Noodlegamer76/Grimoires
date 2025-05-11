package com.noodlegamer76.grimoires.spellcrafting.gui;

import com.noodlegamer76.grimoires.imgui.ChildWindowDragButton;
import com.noodlegamer76.grimoires.imgui.NodeGrabberButton;
import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.InputNode;
import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.Node;
import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.TestNode;
import com.noodlegamer76.grimoires.spellcrafting.Spell;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.type.ImFloat;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static imgui.ImGui.*;

public class SpellEditorTools {
    private final ChildWindowDragButton widthEditor;
    private final ImFloat panelWidth = new ImFloat(150);
    private Spell spell;
    private final SpellEditorRenderer renderer;
    private boolean iONodesOpen = false;
    private boolean calculationNodesOpen = false;
    private boolean conversionNodesOpen = false;
    private static final Map<String, List<NodeGrabberButton<?>>> nodeGrabbers = new HashMap<>();

    public SpellEditorTools(SpellEditorRenderer renderer) {
        this.renderer = renderer;
        widthEditor = new ChildWindowDragButton(10, panelWidth);
        init();
    }

    public void init() {
        nodeGrabbers.clear();

        List<NodeGrabberButton<? extends Node>> iONodes = new ArrayList<>();
        iONodes.add(new NodeGrabberButton<>(-1, "Test Node", TestNode::new, TestNode.class));
        iONodes.add(new NodeGrabberButton<>(-1, "Input", InputNode::new, InputNode.class));
        nodeGrabbers.put("IO Nodes", iONodes);

        List<NodeGrabberButton<? extends Node>> calculationNodes = new ArrayList<>();
        nodeGrabbers.put("Calculation Nodes", calculationNodes);

        List<NodeGrabberButton<? extends Node>> conversionNodes = new ArrayList<>();
        nodeGrabbers.put("Conversion Nodes", conversionNodes);
    }

    public void render() {
        ImVec2 mainEditorSize = ImGui.getContentRegionAvail();
        ImVec2 mousePos = ImGui.getMousePos();
        ImVec2 before = getCursorPos();
        float indent = getStyle().getItemSpacing().x;

        beginChild("Toolbar", panelWidth.get(), mainEditorSize.y, true);

        //render width change button
        widthEditor.render(mainEditorSize);

        if (button("IO Nodes")) {
            iONodesOpen = !iONodesOpen;
        }
        if (iONodesOpen) {
            for (NodeGrabberButton<?> grabberButton: nodeGrabbers.get("IO Nodes")) {
                setCursorPosX(before.x + indent);
                grabberButton.render();
            }
        }

        if (button("Calculation Nodes")) {
            calculationNodesOpen = !calculationNodesOpen;
        }
        if (calculationNodesOpen) {
            for (NodeGrabberButton<?> grabberButton: nodeGrabbers.get("Calculation Nodes")) {
                setCursorPosX(before.x + indent);
                grabberButton.render();
            }
        }

        if (button("Conversion Nodes")) {
            conversionNodesOpen = !conversionNodesOpen;
        }
        if (conversionNodesOpen) {
            for (NodeGrabberButton<?> grabberButton: nodeGrabbers.get("Conversion Nodes")) {
                setCursorPosX(before.x + indent);
                grabberButton.render();
            }
        }

        endChild();
    }

    public Spell getSpell() {
        return spell;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    public static Map<String, List<NodeGrabberButton<?>>> getNodeGrabbers() {
        return nodeGrabbers;
    }
}
