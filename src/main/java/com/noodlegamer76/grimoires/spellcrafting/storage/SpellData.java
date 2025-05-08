package com.noodlegamer76.grimoires.spellcrafting.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.nio.ByteBuffer;

public class SpellData {
    private ByteBuffer data;

    public SpellData(ByteBuffer data) {
        this.data = data;
    }

    public ByteBuffer getData() {
        return data;
    }

    public void setData(ByteBuffer data) {
        this.data = data;
    }

    public static final Codec<SpellData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BYTE_BUFFER.fieldOf("spell_data").forGetter(SpellData::getData)
    ).apply(instance, SpellData::new));

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpellData other)) return false;
        // Compare buffer contents
        ByteBuffer bufA = this.data.asReadOnlyBuffer();
        ByteBuffer bufB = other.data.asReadOnlyBuffer();
        bufA.rewind();
        bufB.rewind();
        if (bufA.remaining() != bufB.remaining()) return false;
        while (bufA.hasRemaining()) {
            if (bufA.get() != bufB.get()) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        ByteBuffer buf = this.data.asReadOnlyBuffer();
        buf.rewind();
        int result = 1;
        while (buf.hasRemaining()) {
            result = 31 * result + buf.get();
        }
        return result;
    }
}
