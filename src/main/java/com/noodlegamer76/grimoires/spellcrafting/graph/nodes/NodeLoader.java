package com.noodlegamer76.grimoires.spellcrafting.graph.nodes;

public class NodeLoader {

    public static Node load(String name) {
        if (name.equals(TestNode.class.getSimpleName())) {
            return new TestNode();
        }
        return null;
    }
}
