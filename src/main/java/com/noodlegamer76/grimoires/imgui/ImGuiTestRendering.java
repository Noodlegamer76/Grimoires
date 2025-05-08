package com.noodlegamer76.grimoires.imgui;

import static imgui.extension.nodeditor.NodeEditor.*;

import imgui.ImGui;
import imgui.extension.nodeditor.NodeEditor;
import imgui.extension.nodeditor.NodeEditorContext;
import imgui.extension.nodeditor.flag.NodeEditorPinKind;
import imgui.type.ImLong;
import org.lwjgl.system.windows.INPUT;

public class ImGuiTestRendering {
    private static final ImGuiTestRendering INSTANCE = new ImGuiTestRendering();
    private static final NodeEditorContext CONTEXT = createEditor();

    public static ImGuiTestRendering getInstance() {
        return INSTANCE;
    }

    public void render() {
        int nodeTest = 1;

        setCurrentEditor(CONTEXT);

        begin("Test Node Editor");

        beginNode(nodeTest++);
        ImGui.text("test");

        beginPin(200, NodeEditorPinKind.Output);
        ImGui.text("out");
        endPin();

        beginPin(201, NodeEditorPinKind.Output);
        ImGui.text("out");
        endPin();

        beginPin(202, NodeEditorPinKind.Output);
        ImGui.text("out");
        endPin();

        endNode();

        beginNode(nodeTest++);
        ImGui.text("test");

        beginPin(100, NodeEditorPinKind.Input);
        ImGui.text("in");
        endPin();

        beginPin(101, NodeEditorPinKind.Input);
        ImGui.text("in");
        endPin();

        beginPin(102, NodeEditorPinKind.Input);
        ImGui.text("in");
        endPin();

        endNode();

        beginCreate();

        endCreate();

        link(100, 102, 200);
        link(1, 200, 100);

        end();
    }
}
