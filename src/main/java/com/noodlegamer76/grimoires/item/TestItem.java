package com.noodlegamer76.grimoires.item;

import com.noodlegamer76.grimoires.imgui.ImGuiScreen;
import com.noodlegamer76.grimoires.spellcrafting.Spell;
import com.noodlegamer76.grimoires.spellcrafting.gui.SpellEditorScreen;
import com.noodlegamer76.grimoires.spellcrafting.storage.SaveSpell;
import com.noodlegamer76.grimoires.spellcrafting.storage.SpellNbtLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TestItem extends Item {
    public TestItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level.isClientSide && !player.isCrouching()) {
            SpellEditorScreen.open(usedHand.ordinal());
            return InteractionResultHolder.success(player.getItemInHand(usedHand));
        }
        if (player.isCrouching()) {
            Spell spell = SpellNbtLoader.getFromItem(player.getItemInHand(usedHand));
            if (spell != null) {
                SaveSpell.saveToFile(spell);
            }
        }

        return super.use(level, player, usedHand);
    }
}
