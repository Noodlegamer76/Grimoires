package com.noodlegamer76.grimoires.imgui;

import com.noodlegamer76.grimoires.spellcrafting.Spell;
import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.Node;
import com.noodlegamer76.grimoires.spellcrafting.gui.SpellEditorRenderer;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.extension.imnodes.ImNodes;
import imgui.flag.ImGuiMouseButton;

public class DraggedNode {
    private final Node node;
    private ImVec2 before;
    private ImVec2 mousePosition;
    private boolean isDragging = false;

    public DraggedNode(Node node, ImVec2 before, ImVec2 mousePosition) {
        this.node = node;
        this.before = before;
        this.mousePosition = mousePosition;
    }

    public void render() {
        if (!ImGui.getIO().getMouseDown(ImGuiMouseButton.Left)) {
            isDragging = false;
            dropped();
            return;
        }

        ImVec2 mousePos = ImGui.getMousePos();
        ImNodes.setNodeScreenSpacePos(getNode().getId(), mousePos.x, mousePos.y);
        node.render();
    }

    public void dropped() {
        Spell spell = SpellEditorRenderer.getInstance().getSpell();
        spell.getGraph().addNode(getNode(), true);
        getNode().setJustDropped(true);

        SpellEditorRenderer.getInstance().setDraggedNode(null);
    }

    public ImVec2 getBefore() {
        return before;
    }

    public ImVec2 getMousePosition() {
        return mousePosition;
    }

    public Node getNode() {
        return node;
    }

    public void setBefore(ImVec2 before) {
        this.before = before;
    }

    public void setMousePosition(ImVec2 mousePosition) {
        this.mousePosition = mousePosition;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    public boolean isDragging() {
        return isDragging;
    }
}
