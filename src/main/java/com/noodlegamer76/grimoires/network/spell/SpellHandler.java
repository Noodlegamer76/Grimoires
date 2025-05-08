package com.noodlegamer76.grimoires.network.spell;

import com.noodlegamer76.grimoires.spellcrafting.Spell;
import com.noodlegamer76.grimoires.spellcrafting.storage.SaveSpell;
import com.noodlegamer76.grimoires.spellcrafting.storage.SpellNbtLoader;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SpellHandler {
    public static void handle(SpellPayload payload, IPayloadContext context) {
        Player player = context.player();
        ItemStack stack = player.getItemInHand(InteractionHand.values()[payload.hand()]);
        Spell spell = payload.spell();

        SpellNbtLoader.saveToItem(stack, spell);
    }
}
