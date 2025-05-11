package com.noodlegamer76.grimoires.spellcrafting.wand;

import com.noodlegamer76.grimoires.item.DebugWand;
import com.noodlegamer76.grimoires.item.WandItem;
import com.noodlegamer76.grimoires.spellcrafting.storage.InitDataComponentType;
import com.noodlegamer76.grimoires.spellcrafting.storage.WandData;
import net.minecraft.world.item.ItemStack;

public class WandCreator {
    public static void createWand(ItemStack stack) {
        if (stack.getItem() instanceof DebugWand) {
            createDebugWand(stack);
        }
    }

    public static void createDebugWand(ItemStack wand) {
        saveToItem(wand, new WandData(10, 10, 100));
    }

    public static void saveToItem(ItemStack wand, WandData data) {
        wand.set(InitDataComponentType.WAND_DATA, data);
    }
}
