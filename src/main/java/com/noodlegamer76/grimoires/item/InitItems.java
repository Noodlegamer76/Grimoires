package com.noodlegamer76.grimoires.item;

import com.noodlegamer76.grimoires.GrimoiresMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.Items.createItems(GrimoiresMod.MODID);

    public static final DeferredItem<TestItem> TEST_ITEM = ITEMS.register("test_item",
            () -> new TestItem(new Item.Properties()));

    public static final DeferredItem<DebugWand> DEBUG_WAND = ITEMS.register("debug_wand",
            () -> new DebugWand(new Item.Properties()));
}
