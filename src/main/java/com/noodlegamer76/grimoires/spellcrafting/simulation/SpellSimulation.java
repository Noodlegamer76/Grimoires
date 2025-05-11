package com.noodlegamer76.grimoires.spellcrafting.simulation;

import com.noodlegamer76.grimoires.spellcrafting.Spell;
import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.Node;
import com.noodlegamer76.grimoires.spellcrafting.simulation.caster.SpellCaster;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class SpellSimulation {
    private final Spell spell;
    private final SpellCaster caster;
    private final NodeSorter nodes;
    //shouldCast is for the current tick while finished is for both the current tick and spell
    private boolean shouldCast = true;
    private boolean finished = false;
    private int nodeCalculations;
    public static final int MAX_NODE_CALCULATIONS = 100;
    private final Level level;
    private int particlesPerTick;
    private int pressurePerParticle;
    private float maxMana;
    private float currentMana;
    private int currentTick = 0;

    public SpellSimulation(Spell spell, SpellCaster caster, int particlesPerTick, int pressurePerParticle, float maxMana) {
        this.spell = spell;
        this.caster = caster;
        nodes = new NodeSorter(getSpell().getGraph(), spell.getGraph().getInputNode());
        this.level = caster.getLevel();
        this.particlesPerTick = particlesPerTick;
        this.pressurePerParticle = pressurePerParticle;
        this.maxMana = maxMana;
        this.currentMana = maxMana;
    }

    public void cast() {
        SpellTicker.addSimulation(this);
    }

    public void tick() {
        if (!shouldCast() || isFinished()) {
            finishTick();
            return;
        }

        int currentParticles = 0;
        for (Node node : spell.getGraph().getNodes()) {
            currentParticles += node.getTotalParticles();
        }
        if (currentParticles == 0 && currentMana <= 0 || spell.getGraph().getInputNode() == null) {
            setFinished(true);
            return;
        }

        List<ManaParticle> input = ManaParticle.createManaParticles(particlesPerTick, getPressurePerParticle());
        for (ManaParticle particle : input) {
            if (currentMana <= 0) {
                break;
            }
            nodes.getInput().addManaParticle(particle);
            currentMana--;
        }

        while (nodeCalculations < MAX_NODE_CALCULATIONS && shouldCast && !finished) {
            Node node = nodes.nextNode();
            if (node != null && node.canProcess(this)) {
                node.process(this);
                splitParticles(node, node.getManaParticlesOutput());
            }

            nodeCalculations++;
        }

        caster.tick(this);
        finishTick();
    }

    public void finishTick() {
        nodeCalculations = 0;
        currentTick++;
    }

    public void splitParticles(Node node, List<ManaParticle> toSplit) {
        int totalParticles = 0;
        List<Node> outgoingNodes = spell.getGraph().getOutgoingNodesFrom(node);
        for (Node outNode : outgoingNodes) {
            totalParticles += outNode.getMaxParticles();
        }

        if (totalParticles == 0) {
            return;
        }

        List<WeightedNode> weightedNodes = new ArrayList<>();
        for (Node outNode : outgoingNodes) {
            float rawWeight = (float) outNode.getMaxParticles() / totalParticles * toSplit.size();
            int baseParticles = (int) Math.floor(rawWeight);
            float fractional = rawWeight - baseParticles;

            weightedNodes.add(new WeightedNode(baseParticles, fractional, outNode));
        }

        for (WeightedNode wn : weightedNodes) {
            for (int i = 0; i < wn.baseParticles && !toSplit.isEmpty(); i++) {
                wn.node.addManaParticle(toSplit.removeFirst());
            }
        }

        weightedNodes.sort((a, b) -> Float.compare(b.fractional, a.fractional));

        int index = 0;
        while (!toSplit.isEmpty()) {
            weightedNodes.get(index % weightedNodes.size()).node.addManaParticle(toSplit.removeFirst());
            index++;
        }
    }


    public Spell getSpell() {
        return spell;
    }

    public SpellCaster getCaster() {
        return caster;
    }

    public void setShouldCast(boolean shouldCast) {
        this.shouldCast = shouldCast;
    }

    public boolean shouldCast() {
        return shouldCast;
    }

    public Level getLevel() {
        return level;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getNodeCalculations() {
        return nodeCalculations;
    }

    public void setParticlesPerTick(int particlesPerTick) {
        this.particlesPerTick = particlesPerTick;
    }

    public int getParticlesPerTick() {
        return particlesPerTick;
    }

    public void setPressurePerParticle(int pressurePerParticle) {
        this.pressurePerParticle = pressurePerParticle;
    }

    public int getPressurePerParticle() {
        return pressurePerParticle;
    }

    private static class WeightedNode {
        final int baseParticles;
        final float fractional;
        final Node node;

        WeightedNode(int baseParticles, float fractional, Node node) {
            this.baseParticles = baseParticles;
            this.fractional = fractional;
            this.node = node;
        }
    }

    public void setMaxMana(float maxMana) {
        this.maxMana = maxMana;
    }

    public float getMaxMana() {
        return maxMana;
    }

    public int getCurrentTick() {
        return currentTick;
    }
}
