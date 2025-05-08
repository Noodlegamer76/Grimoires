package com.noodlegamer76.grimoires.spellcrafting.storage;

import com.noodlegamer76.grimoires.spellcrafting.Spell;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;

public class SpellNbtLoader {
    public static void saveToItem(ItemStack stack, Spell spell) {
        System.out.println(stack);
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        SpellCodec.encodeSpell(buf, spell);

        int len = buf.readableBytes();
        byte[] bytes = new byte[len];
        buf.getBytes(buf.readerIndex(), bytes);

        SpellData newData = new SpellData(ByteBuffer.wrap(bytes));
        stack.set(InitDataComponentType.SPELL_DATA.get(), newData);
    }

    public static @Nullable Spell getFromItem(ItemStack stack) {
        System.out.println(stack);
        SpellData data = stack.get(InitDataComponentType.SPELL_DATA.get());
        if (data == null) return null;

        // Unwrap into a friendly buffer again
        byte[] bytes = data.getData().array();
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(bytes));
        return SpellCodec.decodeSpell(buf);
    }

}
