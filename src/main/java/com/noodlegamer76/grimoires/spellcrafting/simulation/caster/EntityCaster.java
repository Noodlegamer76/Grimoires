package com.noodlegamer76.grimoires.spellcrafting.simulation.caster;

import com.noodlegamer76.grimoires.spellcrafting.simulation.SpellSimulation;
import com.noodlegamer76.grimoires.spellcrafting.simulation.SpellTicker;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class EntityCaster<T extends Entity> extends SpellCaster {
    private final T entity;
    private final ItemStack castItem;

    public EntityCaster(T entity, ItemStack castItem) {
        super(entity.getEyePosition(), entity.getRotationVector(), entity.level());
        this.entity = entity;
        this.castItem = castItem;
    }

    public final T getEntity() {
        return entity;
    }

    public static SpellSimulation getCastingSpellFromItem(ItemStack stack) {
        for (SpellSimulation simulation: SpellTicker.getSimulations()) {
            if (simulation.getCaster() instanceof EntityCaster<?> entityCaster && entityCaster.castItem == stack) {
                return simulation;
            }
        }

        return null;
    }

    public static boolean isCasting(ItemStack stack) {
        return getCastingSpellFromItem(stack) != null;
    }

    @Override
    public void tick(SpellSimulation simulation) {
        if (simulation.isFinished() && entity instanceof LivingEntity livingEntity) {
            livingEntity.stopUsingItem();
        }
    }
}
