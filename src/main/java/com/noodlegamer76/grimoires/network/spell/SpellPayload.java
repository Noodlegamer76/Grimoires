package com.noodlegamer76.grimoires.network.spell;

import com.noodlegamer76.grimoires.GrimoiresMod;
import com.noodlegamer76.grimoires.spellcrafting.Spell;
import com.noodlegamer76.grimoires.spellcrafting.storage.SpellCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SpellPayload(int hand, Spell spell) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SpellPayload> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(GrimoiresMod.MODID, "spell_payload")
    );

    public static final StreamCodec<FriendlyByteBuf, SpellPayload> STREAM_CODEC = new SpellCodec();

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}