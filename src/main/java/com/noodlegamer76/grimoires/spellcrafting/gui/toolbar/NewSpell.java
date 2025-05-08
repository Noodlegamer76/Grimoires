package com.noodlegamer76.grimoires.spellcrafting.gui.toolbar;

import com.noodlegamer76.grimoires.spellcrafting.Spell;
import com.noodlegamer76.grimoires.spellcrafting.SpellManager;
import com.noodlegamer76.grimoires.spellcrafting.gui.SpellEditorRenderer;
import imgui.ImGui;
import imgui.type.ImString;

public class NewSpell {
    private final ImString name = new ImString(128);
    private boolean isVisible = false;

    public void render() {
        if (!isVisible) {
            return;
        }

        ImGui.begin("New Spell");
        ImGui.inputText("Enter name here", name);

        if (!name.isEmpty() && imgui.ImGui.button("Create")) {
            Spell newSpell = new Spell(name.get());
            SpellEditorRenderer.getInstance().setSpell(newSpell);
            name.clear();
            setVisible(false);
        }

        ImGui.end();

    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public ImString getName() {
        return name;
    }
}
