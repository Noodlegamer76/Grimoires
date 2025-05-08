package com.noodlegamer76.grimoires.spellcrafting.graph;

import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.Node;
import com.noodlegamer76.grimoires.spellcrafting.gui.SpellEditorRenderer;
import imgui.extension.imnodes.ImNodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphGuiUtils {

    public static void clearSelection(SpellEditorRenderer renderer) {
        List<Edge> selectedEdges = getSelectedEdges(renderer);
        List<Node> selectedNodes = getSelectedNodes(renderer);

        for (Node node: selectedNodes) {
            selectedEdges.addAll(renderer.getSpell().getGraph().getEdgesFrom(node));
        }

        for (Map.Entry<Node, List<Edge>> edges: renderer.getSpell().getGraph().getAdjacencyList().entrySet()) {
            edges.getValue().removeAll(selectedEdges);
        }

        for (Edge edge: selectedEdges) {
            renderer.getSpell().getGraph().removeEdge(edge);
        }

        for (Node node: selectedNodes) {
            renderer.getSpell().getGraph().removeNode(node);
        }

        ImNodes.clearLinkSelection();
        ImNodes.clearNodeSelection();
    }

    public static List<Node> getSelectedNodes(SpellEditorRenderer renderer) {
        List<Node> nodes = new ArrayList<>();
        for (Node node : renderer.getSpell().getGraph().getNodes()) {
            if (ImNodes.isNodeSelected(node.getId())) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    public static List<Edge> getSelectedEdges(SpellEditorRenderer renderer) {
        List<Edge> edges = new ArrayList<>();
        for (Edge edge: renderer.getSpell().getGraph().getAllEdges()) {
            if (ImNodes.isLinkSelected(edge.id())) {
                edges.add(edge);
            }
        }
        return edges;
    }
}
