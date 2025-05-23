package com.noodlegamer76.grimoires;

import com.mojang.logging.LogUtils;
import com.noodlegamer76.grimoires.imgui.ImGuiFontLoader;
import com.noodlegamer76.grimoires.spellcrafting.SpellManager;
import com.noodlegamer76.grimoires.item.InitItems;
import com.noodlegamer76.grimoires.spellcrafting.Spell;
import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.InitNodes;
import com.noodlegamer76.grimoires.spellcrafting.storage.InitDataComponentType;
import com.noodlegamer76.grimoires.spellcrafting.storage.SaveSpell;
import com.noodlegamer76.grimoires.spellcrafting.storage.SpellFileConstants;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;

import java.io.File;
import java.util.List;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(GrimoiresMod.MODID)
public class GrimoiresMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "grimoires";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public GrimoiresMod(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        InitItems.ITEMS.register(modEventBus);
        InitDataComponentType.DATA_COMPONENT_TYPES.register(modEventBus);
        InitNodes.init();

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (Grimoires) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        SpellFileConstants.SERVER_INSTANCE_LOCATION = event.getServer().getServerDirectory().toAbsolutePath() + File.separator;
        List<Spell> spells = SaveSpell.getAllSpells();
        SpellManager.addAllSpells(spells);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            SpellFileConstants.CLIENT_INSTANCE_LOCATION = Minecraft.getInstance().gameDirectory.getAbsoluteFile().toString();
            List<Spell> spells = SaveSpell.getAllSpells();
            SpellManager.addAllSpells(spells);
        }
    }
}
