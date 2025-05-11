package com.noodlegamer76.grimoires.spellcrafting.simulation;

import com.noodlegamer76.grimoires.GrimoiresMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber(modid = GrimoiresMod.MODID)
public class SpellTicker {
    private static final List<SpellSimulation> simulations = new ArrayList<>();

    @SubscribeEvent
    public static void tickSpells(ServerTickEvent.Post event) {
        Iterator<SpellSimulation> it = simulations.iterator();
        while (it.hasNext()) {
            SpellSimulation simulation = it.next();
            if (!simulation.isFinished()) {
                simulation.tick();
            } else {
                it.remove();
            }
        }
    }

    public static List<SpellSimulation> getSimulations() {
        return simulations;
    }

    public static void addSimulation(SpellSimulation simulation) {
        simulations.add(simulation);
    }

    public static void stopSimulation(SpellSimulation simulation) {
        simulation.setFinished(true);
        simulation.setShouldCast(false);
    }
}
