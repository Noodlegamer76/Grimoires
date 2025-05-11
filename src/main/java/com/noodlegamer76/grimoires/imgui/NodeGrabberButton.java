package com.noodlegamer76.grimoires.imgui;

import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.Node;
import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.NodeHolder;
import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.NodeRegistryUtils;
import com.noodlegamer76.grimoires.spellcrafting.gui.SpellEditorRenderer;
import imgui.ImGui;
import imgui.flag.ImGuiCol;

import java.util.function.Supplier;

public class NodeGrabberButton<T extends Node> {
    private final int max;
    private int current;
    private final String name;
    private final Supplier<T> nodeSupplier;
    private final NodeHolder<? extends Node> nodeHolder;

    public NodeGrabberButton(int max, String name, Supplier<T> nodeSupplier, Class<T> nodeClass) {
        this.max = max;
        this.name = name;
        this.nodeSupplier = nodeSupplier;
        nodeHolder = NodeRegistryUtils.getNodeHolder(nodeClass);
    }

    public void renderNoLimits() {
        ImGui.button(name);

        if (ImGui.isItemActivated()) {
            Node node = nodeSupplier.get();
            node.setId(SpellEditorRenderer.getInstance().getSpell().getGraph().nextId());
            SpellEditorRenderer.getInstance().setDraggedNode(node);
        }
    }

    public void render() {
        if (nodeHolder != null && nodeHolder.isLocked()) {
            return;
        }

        if (max == -1) {
            ImGui.button(name);
        }
        else {
            if (current == max) {
                ImGui.pushStyleColor(ImGuiCol.Button, ImGui.getColorU32(0.75f, 0f, 0f, 0.5f));
                ImGui.button(name + " " + current + "/" + max);
                ImGui.popStyleColor();
            }
            else {
                ImGui.button(name + " " + current + "/" + max);
            }
        }

        if ((current < max || max == -1) && ImGui.isItemActivated()) {
            Node node = nodeSupplier.get();
            node.setId(SpellEditorRenderer.getInstance().getSpell().getGraph().nextId());
            node.initForSpellEditor(0);
            SpellEditorRenderer.getInstance().setDraggedNode(node);
            if (max != -1) {
                current++;
            }
        }
    }

    public String getName() {
        return name;
    }
}
