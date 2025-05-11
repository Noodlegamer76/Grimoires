package com.noodlegamer76.grimoires.spellcrafting.storage;

import com.noodlegamer76.grimoires.GrimoiresMod;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitDataComponentType {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, GrimoiresMod.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SpellData>> SPELL_DATA = DATA_COMPONENT_TYPES.register("spell_data",
            () -> new DataComponentType.Builder<SpellData>()
                    .persistent(SpellData.CODEC)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WandData>> WAND_DATA = DATA_COMPONENT_TYPES.register("wand_data",
            () -> new DataComponentType.Builder<WandData>()
                    .networkSynchronized(WandData.STREAM_CODEC)
                    .persistent(WandData.CODEC)
                    .build());
}
