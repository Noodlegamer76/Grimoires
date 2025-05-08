package com.noodlegamer76.grimoires.spellcrafting.gui;

import imgui.ImGui;
import imgui.extension.imnodes.ImNodes;
import imgui.extension.imnodes.flag.ImNodesColorStyle;
import imgui.flag.ImGuiCol;

public class SpellEditorStyle {

    public static void pushStyle() {
        ImGui.pushStyleColor(ImGuiCol.WindowBg, ImGui.getColorU32(0f, 0f, 0f, 0.6f));
        //ImNodes.pushColorStyle(ImNodesColorStyle.NodeBackground, ImGui.getColorU32(0.77647058823f, 0.77647058823f, 0.77647058823f, 1f));
    }

    public static void popStyle() {
        ImGui.popStyleColor();
        //ImNodes.popColorStyle();
    }
}
