package com.noodlegamer76.grimoires.network;

import com.noodlegamer76.grimoires.GrimoiresMod;
import com.noodlegamer76.grimoires.network.spell.SpellHandler;
import com.noodlegamer76.grimoires.network.spell.SpellPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = GrimoiresMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RegisterPayloads {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                SpellPayload.TYPE,
                SpellPayload.STREAM_CODEC,
                SpellHandler::handle
        );

    }
}