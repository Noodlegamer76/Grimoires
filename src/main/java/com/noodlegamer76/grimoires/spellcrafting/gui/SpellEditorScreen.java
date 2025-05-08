package com.noodlegamer76.grimoires.spellcrafting.gui;

import com.noodlegamer76.grimoires.imgui.ImGuiRenderer;
import com.noodlegamer76.grimoires.imgui.ImGuiScreen;
import com.noodlegamer76.grimoires.imgui.ImGuiTestRendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class SpellEditorScreen extends ImGuiScreen {

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        ImGuiRenderer.getInstance().draw(() -> SpellEditorRenderer.getInstance().render());
    }

    public static void open(int hand) {
        SpellEditorRenderer.getInstance().setHand(hand);
        Minecraft.getInstance().setScreen(new SpellEditorScreen());
    }
}
