package com.noodlegamer76.grimoires.spellcrafting.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public class WandData {
    private final int particlesPerTick;
    private final int pressurePerParticle;
    private final float maxMana;

    public WandData(int particlesPerTick, int pressurePerParticle, float maxMana) {
        this.particlesPerTick = particlesPerTick;
        this.pressurePerParticle = pressurePerParticle;
        this.maxMana = maxMana;
    }

    public static final Codec<WandData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("particles_per_tick").forGetter(WandData::getParticlesPerTick),
            Codec.INT.fieldOf("pressure_per_particle").forGetter(WandData::getPressurePerParticle),
            Codec.FLOAT.fieldOf("max_mana").forGetter(WandData::getMaxMana)
    ).apply(instance, WandData::new));

    public static final StreamCodec<FriendlyByteBuf, WandData> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public void encode(FriendlyByteBuf buffer, WandData value) {
            buffer.writeInt(value.getParticlesPerTick());
            buffer.writeInt(value.getPressurePerParticle());
            buffer.writeFloat(value.getMaxMana());
        }

        @Override
        public WandData decode(FriendlyByteBuf buffer) {
            return new WandData(buffer.readInt(), buffer.readInt(), buffer.readFloat());
        }
    };

    public int getParticlesPerTick() {
        return particlesPerTick;
    }

    public int getPressurePerParticle() {
        return pressurePerParticle;
    }

    public float getMaxMana() {
        return maxMana;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WandData wandData = (WandData) o;
        return particlesPerTick == wandData.particlesPerTick &&
                pressurePerParticle == wandData.pressurePerParticle &&
                Float.compare(wandData.maxMana, maxMana) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(particlesPerTick, pressurePerParticle, maxMana);
    }
}
