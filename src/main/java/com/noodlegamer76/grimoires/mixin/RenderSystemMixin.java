package com.noodlegamer76.grimoires.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import com.noodlegamer76.grimoires.GrimoiresMod;
import com.noodlegamer76.grimoires.imgui.ImGuiFontLoader;
import com.noodlegamer76.grimoires.imgui.ImGuiRenderer;
import imgui.ImFont;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Mixin(RenderSystem.class)
public abstract class RenderSystemMixin {

    @Inject( at = @At( value = "TAIL" ), method = "initRenderer(IZ)V" )
    private static void initRenderer(int flags, boolean enable, CallbackInfo cbi) {
        RenderSystem.assertOnRenderThread();
        LogUtils.getLogger().info("Initializing ImGui");
        ImGuiRenderer.getInstance().init(RenderSystemMixin::loadSettings);
    }

    private static void loadSettings() {
        ImGuiIO io = ImGui.getIO();
        // ImGui.getIO().addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
    }



    @Inject( at = @At( value = "HEAD"), method = "flipFrame(J)V" )
    private static void flipFrame(long pWindowId, CallbackInfo ci) {
        RenderSystem.recordRenderCall(() -> ImGuiRenderer.getInstance().render());
    }
}
