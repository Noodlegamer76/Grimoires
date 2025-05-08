package com.noodlegamer76.grimoires.imgui;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

@OnlyIn(Dist.CLIENT)
public class ImGuiRenderer {
    private static ImGuiRenderer instance = null;

    public static ImGuiRenderer getInstance() {
        if (instance == null) {
            instance = new ImGuiRenderer();
        }
        return instance;
    }

    private final ArrayList<ImGuiCall> _preDrawCalls = new ArrayList<>();
    private final ArrayList<ImGuiCall> _drawCalls = new ArrayList<>();
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl = new ImGuiImplGl3();
    private boolean isFontUploaded = false;

    private ImGuiRenderer() {
    }

    public void init() {
        init(()->{});
    }

    public void init(ImGuiCall config) {
        ImGui.createContext();
        config.execute();
        imGuiGlfw.init(Minecraft.getInstance().getWindow().getWindow(), false);
        imGuiGl.init();
    }

    public void preDraw(ImGuiCall drawCall) {
        _preDrawCalls.add(drawCall);
    }

    public void draw(ImGuiCall drawCall) {
        _drawCalls.add(drawCall);
    }

    public void render() {
        if (!isFontUploaded) {
            isFontUploaded = true;
            ImGuiFontLoader.loadFont();
            ImGuiFontLoader.uploadFontTexture();
        }

        for(ImGuiCall preDrawCall : _preDrawCalls) {
            preDrawCall.execute();
        }
        _preDrawCalls.clear();

        imGuiGlfw.newFrame();
        ImGui.newFrame();

        if (isFontUploaded) {
            ImGui.pushFont(ImGuiFontLoader.font);
        }

        // Render ImGui Here
        for(ImGuiCall drawCall : _drawCalls) {
            drawCall.execute();
        }

        if (isFontUploaded) {
            ImGui.popFont();
        }

        _drawCalls.clear();

        ImGui.render();
        if (ImGui.getDrawData() != null) {
            imGuiGl.renderDrawData(ImGui.getDrawData());
        }
        else {
            System.out.println("ImGui draw data is null");
        }

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
    }
}
