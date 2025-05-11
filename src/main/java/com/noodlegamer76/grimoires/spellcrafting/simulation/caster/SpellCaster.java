package com.noodlegamer76.grimoires.spellcrafting.simulation.caster;

import com.noodlegamer76.grimoires.spellcrafting.simulation.SpellSimulation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public abstract class SpellCaster {
    private final Vec3 position;
    private final Vec2 direction;
    private final Level level;

    public SpellCaster(Vec3 position, Vec2 direction, Level level) {
        this.position = position;
        this.direction = direction;
        this.level = level;
    }

    public final Vec3 getPosition() {
        return position;
    }

    public final Vec2 getDirection() {
        return direction;
    }

    public final Level getLevel() {
        return level;
    }

    public abstract void tick(SpellSimulation simulation);
}
