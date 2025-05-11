package com.noodlegamer76.grimoires.spellcrafting.graph.nodes;

import com.noodlegamer76.grimoires.GrimoiresMod;
import com.noodlegamer76.grimoires.spellcrafting.simulation.SpellSimulation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;

public class InputNode extends Node {
    public InputNode() {
        super("Wand Input", MAX_NODE_PARTICLES);
        setInput(true);
    }

    @Override
    public boolean canProcess(SpellSimulation simulation) {
        return hasManaParticles();
    }

    @Override
    public void process(SpellSimulation simulation) {
        addManaParticlesOutput(getManaParticles());
        getManaParticles().clear();

        //System.out.println("InputNode processed: Calculation number " + simulation.getNodeCalculations() + ", \nCurrent total particles in node: " + getManaParticles().size() + " currently in node, \n" + getManaParticlesOutput().size() + " currently in output\n Tick number: " + simulation.getCurrentTick());
    }
}
