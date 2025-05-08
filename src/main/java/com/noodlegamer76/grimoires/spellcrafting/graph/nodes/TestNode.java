package com.noodlegamer76.grimoires.spellcrafting.graph.nodes;

import com.noodlegamer76.grimoires.imgui.pins.NodePin;
import com.noodlegamer76.grimoires.spellcrafting.Spell;
import com.noodlegamer76.grimoires.spellcrafting.gui.SpellEditorRenderer;
import imgui.extension.imnodes.flag.ImNodesPinShape;

public class TestNode extends Node {
    private static final Spell spell = SpellEditorRenderer.getInstance().getSpell();

    public TestNode(int id) {
        super(id, "Test Node");
    }

    public TestNode() {
        super("Test Node");
    }

    @Override
    public void init() {
        addPin(new NodePin<>(NodePin.IOType.INPUT, spell.getGraph().nextId(), Integer.class, ImNodesPinShape.Triangle, "Int Input"));
        addPin(new NodePin<>(NodePin.IOType.INPUT, spell.getGraph().nextId(), Boolean.class, ImNodesPinShape.Quad, "Boolean Input"));

        addPin(new NodePin<>(NodePin.IOType.OUTPUT, spell.getGraph().nextId(), Integer.class, ImNodesPinShape.TriangleFilled, "Integer Output 1"));
        addPin(new NodePin<>(NodePin.IOType.OUTPUT, spell.getGraph().nextId(), Integer.class, ImNodesPinShape.TriangleFilled, "Integer Output 2"));
        addPin(new NodePin<>(NodePin.IOType.OUTPUT, spell.getGraph().nextId(), Boolean.class, ImNodesPinShape.QuadFilled, "Boolean Output"));
    }
}
