package com.noodlegamer76.grimoires.spellcrafting.simulation.caster;

import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public abstract class SpellCaster {
    private final Vector3f position;
    private final Level level;

    public SpellCaster(Vector3f position, Level level) {
        this.position = position;
        this.level = level;
    }

    public final Level getLevel() {
        return level;
    }

    public final Vector3f getPosition() {
        return position;
    }
}
