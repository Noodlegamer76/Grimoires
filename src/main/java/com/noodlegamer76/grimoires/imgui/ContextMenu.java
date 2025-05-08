package com.noodlegamer76.grimoires.imgui;

import com.noodlegamer76.grimoires.spellcrafting.graph.GraphGuiUtils;
import com.noodlegamer76.grimoires.spellcrafting.gui.SpellEditorRenderer;
import com.noodlegamer76.grimoires.spellcrafting.gui.SpellEditorTools;
import imgui.ImGui;
import imgui.flag.ImGuiMouseButton;

public class ContextMenu {
    SpellEditorRenderer renderer;
    SpellEditorTools tools;

    public ContextMenu(SpellEditorRenderer renderer, SpellEditorTools tools) {
        this.renderer = renderer;
        this.tools = tools;
    }

    public void render() {

        if (ImGui.isMouseClicked(ImGuiMouseButton.Right) && ImGui.isWindowHovered()) {
            ImGui.openPopup("context_menu");
        }

        if (ImGui.beginPopup("context_menu")) {
            ImGui.text("Context Menu");

            if (ImGui.button("Delete Selected")) {
                GraphGuiUtils.clearSelection(renderer);
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }
    }
}
