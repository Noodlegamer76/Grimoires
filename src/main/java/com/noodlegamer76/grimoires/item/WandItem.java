package com.noodlegamer76.grimoires.item;

import com.noodlegamer76.grimoires.GrimoiresMod;
import com.noodlegamer76.grimoires.spellcrafting.Spell;
import com.noodlegamer76.grimoires.spellcrafting.gui.SpellEditorScreen;
import com.noodlegamer76.grimoires.spellcrafting.simulation.SpellSimulation;
import com.noodlegamer76.grimoires.spellcrafting.simulation.SpellTicker;
import com.noodlegamer76.grimoires.spellcrafting.simulation.caster.EntityCaster;
import com.noodlegamer76.grimoires.spellcrafting.simulation.caster.SpellCaster;
import com.noodlegamer76.grimoires.spellcrafting.storage.InitDataComponentType;
import com.noodlegamer76.grimoires.spellcrafting.storage.SaveSpell;
import com.noodlegamer76.grimoires.spellcrafting.storage.SpellNbtLoader;
import com.noodlegamer76.grimoires.spellcrafting.storage.WandData;
import com.noodlegamer76.grimoires.spellcrafting.wand.WandCreator;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class WandItem extends Item {
    public WandItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return Integer.MAX_VALUE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            WandData data = stack.get(InitDataComponentType.WAND_DATA);
            if (data == null) {
                WandCreator.createDebugWand(stack);
            }
        }

        if (level.isClientSide && player.isCrouching()) {
            SpellEditorScreen.open(usedHand.ordinal());
            return InteractionResultHolder.success(stack);
        }
        else if (!level.isClientSide && !player.isCrouching()) {
            Spell spell = SpellNbtLoader.getFromItem(stack);
            if (spell != null) {
                WandData data = stack.get(InitDataComponentType.WAND_DATA);
                if (data == null) {
                    GrimoiresMod.LOGGER.error("Wand data is null, How did this happen?");
                    return InteractionResultHolder.fail(stack);
                }
                SpellCaster caster = new EntityCaster<>(player, stack);
                SpellSimulation simulation = new SpellSimulation(spell, caster, data.getParticlesPerTick(), data.getPressurePerParticle(), data.getMaxMana());
                simulation.cast();
                player.startUsingItem(usedHand);
                return InteractionResultHolder.consume(stack);
            }
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        super.releaseUsing(stack, level, livingEntity, timeCharged);
        if (!level.isClientSide) {
            SpellSimulation simulation = EntityCaster.getCastingSpellFromItem(stack);
            if (simulation != null) {
                SpellTicker.stopSimulation(simulation);
            }
        }
    }
}
