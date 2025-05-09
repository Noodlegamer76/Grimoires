package com.noodlegamer76.grimoires.spellcrafting.simulation.caster;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;

public class EntityCaster extends SpellCaster {
    private final Entity entity;

    public EntityCaster(Entity entity) {
        super(entity.getEyePosition().toVector3f(), entity.level());
        this.entity = entity;
    }
}
