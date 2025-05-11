package com.noodlegamer76.grimoires.spellcrafting.graph.nodes;

import com.noodlegamer76.grimoires.imgui.pins.NodePin;

import java.util.ArrayList;
import java.util.List;

public class NodeRegistryUtils {
    private static final List<NodeHolder<? extends Node>> NODES = new ArrayList<>();

    public static <T extends Node> NodeHolder<T> register(NodeHolder<T> nodeHolder) {
        NODES.add(nodeHolder);
        return nodeHolder;
    }

    public static Node getNode(String name) {
        for (NodeHolder<? extends Node> node: NODES) {
            if (node.getName().equals(name)) {
                return node.newNode();
            }
        }

        return null;
    }

    public static List<NodePin<?>> getPinsFromNodeClass(Class<? extends Node> nodeClass, int variant) {
        List<NodePin<?>> pins = new ArrayList<>();
        for (NodeHolder<? extends Node> node: NODES) {
            if (nodeClass == node.getNodeClass()) {
                node.getPinRegistry().getPins(variant).forEach(supplier -> pins.add(supplier.get()));
            }
        }
        return pins;
    }

    public static List<NodeHolder<? extends Node>> getNODES() {
        return NODES;
    }

    public static <T extends Node> NodeHolder<T> getNodeHolder(Class<T> nodeClass) {
        for (NodeHolder<? extends Node> node: NODES) {
            if (nodeClass == node.getNodeClass()) {
                return (NodeHolder<T>) node;
            }
        }

        return null;
    }
}
