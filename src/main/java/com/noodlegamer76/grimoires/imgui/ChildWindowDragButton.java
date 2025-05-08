package com.noodlegamer76.grimoires.imgui;

import imgui.ImVec2;
import imgui.flag.ImGuiMouseButton;
import imgui.type.ImFloat;

import static imgui.ImGui.*;

public class ChildWindowDragButton {
    final float gripWidth;
    ImFloat panelWidth;
    boolean isDragging = false;

    public ChildWindowDragButton(float gripWidth, ImFloat panelWidth) {
        this.gripWidth = gripWidth;
        this.panelWidth = panelWidth;
    }

    public void render(ImVec2 mainWindowSize) {
        ImVec2 before = getCursorPos();
        setCursorPosX(panelWidth.get() - gripWidth);

        button(">", gripWidth, mainWindowSize.y);

        if (isItemActivated()) {
            isDragging = true;
        }
        if (isDragging) {
            panelWidth.set(panelWidth.get() + getIO().getMouseDeltaX());
            panelWidth.set(Math.clamp(panelWidth.get(), 150, mainWindowSize.x / 2));
        }
        if (isMouseReleased(ImGuiMouseButton.Left)) {
            isDragging = false;
        }

        setCursorPos(before.x, before.y);
    }
}
