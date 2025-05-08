package com.noodlegamer76.grimoires.spellcrafting.gui.toolbar;

import com.noodlegamer76.grimoires.network.spell.SpellPayload;
import com.noodlegamer76.grimoires.spellcrafting.SpellManager;
import com.noodlegamer76.grimoires.spellcrafting.Spell;
import com.noodlegamer76.grimoires.spellcrafting.gui.SpellEditorRenderer;
import com.noodlegamer76.grimoires.spellcrafting.storage.SaveSpell;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.PacketDistributor;

import static imgui.ImGui.*;

public class SpellEditorToolBar {
    NewSpell newSpell = new NewSpell();

    public void render() {
        beginMainMenuBar();

        Spell currentSpell = SpellEditorRenderer.getInstance().getSpell();

        if (beginMenu("Spell")) {
            if (beginMenu("New Spell")) {
                if (beginMenu("Make sure you save first!!!")) {
                    if (menuItem("Confirm")) {
                        newSpell.setVisible(true);
                    }
                    endMenu();
                }
                endMenu();
            }
            if (beginMenu("Open Spell")) {
                for (Spell spell: SpellManager.getSpells()) {
                    if (menuItem(spell.getName())) {
                        spell = SaveSpell.loadFromFile(spell.getName());
                        SpellEditorRenderer.getInstance().setSpell(spell);
                    }
                }
                endMenu();
            }

            boolean containsName = false;
            for (Spell spell: SpellManager.getSpells()) {
                if (spell.getName().equals(currentSpell.getName())) {
                    containsName = true;
                    break;
                }
            }

            if (containsName) {
                if (beginMenu("Save Spell")) {
                    if (menuItem("Overwrite: " + currentSpell.getName())) {
                        SpellManager.removeSpell(currentSpell);
                        SpellManager.addSpell(currentSpell);
                        currentSpell.addPositions();
                        SaveSpell.saveToFile(currentSpell);
                    }
                    endMenu();
                }
            }
            else if (menuItem("Save Spell")) {
                SpellManager.removeSpell(currentSpell);
                SpellManager.addSpell(currentSpell);
                currentSpell.addPositions();
                SaveSpell.saveToFile(currentSpell);
            }


            endMenu();
        }

        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getPermissionLevel() > 0 && menuItem("Upload Spell")) {
            PacketDistributor.sendToServer(new SpellPayload(SpellEditorRenderer.getInstance().getHand(), currentSpell));
        }

        endMainMenuBar();

        if (newSpell.isVisible()) {
            newSpell.render();
        }
    }
}
