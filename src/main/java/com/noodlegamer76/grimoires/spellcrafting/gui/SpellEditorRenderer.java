package com.noodlegamer76.grimoires.spellcrafting.gui;

import com.noodlegamer76.grimoires.imgui.ContextMenu;
import com.noodlegamer76.grimoires.imgui.DraggedNode;
import com.noodlegamer76.grimoires.imgui.pins.NodePin;
import com.noodlegamer76.grimoires.spellcrafting.Spell;
import com.noodlegamer76.grimoires.spellcrafting.graph.Edge;
import com.noodlegamer76.grimoires.spellcrafting.graph.nodes.Node;
import com.noodlegamer76.grimoires.spellcrafting.gui.toolbar.SpellEditorToolBar;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.extension.imnodes.ImNodes;
import imgui.extension.imnodes.ImNodesContext;
import imgui.extension.imnodes.flag.ImNodesMiniMapLocation;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;

import javax.annotation.Nullable;
import java.util.Map;

public class SpellEditorRenderer {
    private final SpellEditorTools spellEditorTools = new SpellEditorTools(this);
    private static final SpellEditorRenderer INSTANCE = new SpellEditorRenderer();
    private final SpellEditorToolBar TOOLBAR = new SpellEditorToolBar();
    private final ContextMenu contextMenu;
    private Spell spell = new Spell("Untitled");
    private DraggedNode draggedNode;
    private final ImNodesContext context;
    private boolean setPositions = false;
    private int hand;

    public static SpellEditorRenderer getInstance() {
        return INSTANCE;
    }

    public SpellEditorRenderer() {
        ImNodes.createContext();
        context = ImNodes.editorContextCreate();
        spellEditorTools.setSpell(spell);
        contextMenu = new ContextMenu(this, spellEditorTools);
    }

    public void render() {
        TOOLBAR.render();

        if (spell == null) {
            return;
        }

        if (setPositions) {
            for (Map.Entry<Integer, ImVec2> position: spell.getNodePositions().entrySet()) {
                ImNodes.setNodeEditorSpacePos(position.getKey(), position.getValue().x, position.getValue().y);
            }
            setPositions = false;
        }

        int windowFlags =
                ImGuiWindowFlags.MenuBar |
                        ImGuiWindowFlags.NoDecoration |
                        ImGuiWindowFlags.NoSavedSettings |
                        ImGuiWindowFlags.NoResize |
                        ImGuiWindowFlags.NoCollapse |
                        ImGuiWindowFlags.NoMove |
                        ImGuiWindowFlags.NoBringToFrontOnFocus |
                        ImGuiWindowFlags.NoNavFocus;

        SpellEditorStyle.pushStyle();

        ImGui.setNextWindowPos(ImGui.getMainViewport().getWorkPosX(), ImGui.getMainViewport().getWorkPosY());
        ImGui.setNextWindowSize(ImGui.getMainViewport().getWorkSizeX(), ImGui.getMainViewport().getWorkSizeY());
        ImGui.begin("Spell Editor: " + spell.getName(), windowFlags);

        ImNodes.editorContextSet(context);
        ImNodes.beginNodeEditor();

        ImVec2 mousePos = ImGui.getMousePos();

        for (Node node: spell.getGraph().getNodes()) {
            node.render();
        }

        for (Edge edge: getSpell().getGraph().getAllEdges()) {
            ImNodes.link(edge.id(), edge.from().getId(), edge.to().getId());
        }

        //render spell tools window
        ImVec2 before = ImGui.getCursorPos();
        ImGui.setCursorPos(0, 0);
        spellEditorTools.render();
        ImGui.setCursorPos(before.x, before.y);

        //render dragged node
        if (draggedNode != null) {
            draggedNode.setMousePosition(mousePos);
            draggedNode.setBefore(ImGui.getCursorPos());
            draggedNode.render();
        }

        contextMenu.render();

        ImNodes.miniMap(0.25f, ImNodesMiniMapLocation.TopRight);
        ImNodes.endNodeEditor();

        ImGui.end();

        SpellEditorStyle.popStyle();

        ImInt fromId = new ImInt();
        ImInt toId = new ImInt();
        if (ImNodes.isLinkCreated(fromId, toId)) {
            NodePin<?> from = spell.getGraph().getNodePinById(fromId.get());
            NodePin<?> to = spell.getGraph().getNodePinById(toId.get());

            if (from != null && to != null && from.getType() == to.getType()) {
                spell.getGraph().addEdge(from, to);
            }
            else {
                System.out.println("from: " + from + " to: " + to);
            }
        }

        ImInt destroyedLinkId = new ImInt();
        if (ImNodes.isLinkDestroyed(destroyedLinkId)) {
            spell.getGraph().removeEdge(getSpell().getGraph().getEdgeById(destroyedLinkId.get()));
        }


    }

    public void setDraggedNode(@Nullable Node draggedNode) {
        if (draggedNode == null) {
            this.draggedNode = null;
            return;
        }

        this.draggedNode = new DraggedNode(draggedNode, new ImVec2(), new ImVec2());
        this.draggedNode.setDragging(true);
    }

    public Spell getSpell() {
        return spell;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
        setPositions = true;
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public void setHand(int hand) {
        this.hand = hand;
    }

    public int getHand() {
        return hand;
    }
}
