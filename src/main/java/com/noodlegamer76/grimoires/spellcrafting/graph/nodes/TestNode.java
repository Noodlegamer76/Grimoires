package com.noodlegamer76.grimoires.spellcrafting.graph.nodes;

import com.noodlegamer76.grimoires.imgui.pins.NodePin;
import com.noodlegamer76.grimoires.spellcrafting.Spell;
import com.noodlegamer76.grimoires.spellcrafting.gui.SpellEditorRenderer;
import com.noodlegamer76.grimoires.spellcrafting.simulation.SpellSimulation;
import imgui.extension.imnodes.flag.ImNodesPinShape;

public class TestNode extends Node {

    public TestNode() {
        super("Test Node", MAX_NODE_PARTICLES);
    }

    @Override
    public boolean canProcess(SpellSimulation simulation) {
        return (simulation.getNodeCalculations() == 1 || simulation.getNodeCalculations() == 2 || simulation.getNodeCalculations() == 3);
    }

    @Override
    public void process(SpellSimulation simulation) {
        System.out.println("\ntick number: " + simulation.getCurrentTick() + ", \nnode calculations: " + simulation.getNodeCalculations() + ", \nMana: " + getManaParticles().size());
    }
}
