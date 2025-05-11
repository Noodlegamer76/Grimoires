package com.noodlegamer76.grimoires.spellcrafting.graph.nodes;

import com.noodlegamer76.grimoires.imgui.pins.NodePin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class PinRegistry {
    private final Map<Integer, List<Supplier<NodePin<?>>>> pins = new HashMap<>();

    public PinRegistry addPin(int variant, List<Supplier<NodePin<?>>> pinList) {
        pins.put(variant, pinList);
        return this;
    }

    public Map<Integer, List<Supplier<NodePin<?>>>> getPins() {
        return pins;
    }

    public List<Supplier<NodePin<?>>> getPins(int variant) {
        return pins.get(variant);
    }
}
