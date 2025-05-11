package com.noodlegamer76.grimoires.spellcrafting.graph.nodes;

import com.noodlegamer76.grimoires.imgui.pins.NodePin;
import com.noodlegamer76.grimoires.spellcrafting.graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class NodeHolder<T extends Node> {
    private final Supplier<? extends T> supplier;
    private final Class<T> nodeClass;
    private final PinRegistry pinRegistry;
    private final int id;
    private final boolean lockedByDefault;
    private boolean locked;

    public NodeHolder(Class<T> nodeClass, Supplier<? extends T> supplier, int id, boolean lockedByDefault, PinRegistry pins) {
        this.nodeClass = nodeClass;
        this.supplier = supplier;
        this.pinRegistry = pins;
        this.id = id;
        this.lockedByDefault = lockedByDefault;
        this.locked = lockedByDefault;
    }

    public T newNode() {
        return supplier.get();
    }

    public String getName() {
        return nodeClass.getSimpleName();
    }

    public Class<T> getNodeClass() {
        return nodeClass;
    }

    public PinRegistry getPinRegistry() {
        return pinRegistry;
    }

    public int getId() {
        return id;
    }

    public boolean isLockedByDefault() {
        return lockedByDefault;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
