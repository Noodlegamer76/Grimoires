package com.noodlegamer76.grimoires.imgui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.noodlegamer76.grimoires.GrimoiresMod;
import imgui.ImFont;
import imgui.ImGui;
import imgui.type.ImInt;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class ImGuiFontLoader {
    public static int fontTextureId = -1;
    public static boolean fontBuilt = false;
    public static ImFont font;

    public static void loadFont() {
        try {
            GrimoiresMod.LOGGER.info("Loading Minecraft font for ImGui");
            ResourceLocation fontResource = ResourceLocation.fromNamespaceAndPath(GrimoiresMod.MODID, "fonts/minecraft_font.ttf");
            InputStream stream = Minecraft.getInstance().getResourceManager().open(fontResource);

            byte[] fontBytes = stream.readAllBytes();
            stream.close(); // Always good practice to close the stream

            // Add the font to ImGui with the desired pixel height (16.0f is typical)
            font = ImGui.getIO().getFonts().addFontFromMemoryTTF(fontBytes, 13);

            ImGui.getIO().getFonts().build();
            fontBuilt = true;

            GrimoiresMod.LOGGER.info("Finished loading Minecraft font for ImGui");

        } catch (IOException e) {
            throw new RuntimeException("Grimoires: Failed to load Minecraft font for ImGui", e);
        }
        //File fontFile = new File("C:\\Users\\ab098\\IdeaProjects\\grimoires\\src\\main\\resources\\assets\\grimoires\\fonts\\minecraft_font.ttf");
        //System.out.println(fontFile.getAbsolutePath());
        //System.out.println(fontFile.getPath());
        //System.out.println(fontFile);
        //return ImGui.getIO().getFonts().addFontFromFileTTF(fontFile.getAbsolutePath(), 16.0f);
    }

    public static void uploadFontTexture() {
        GrimoiresMod.LOGGER.info("Uploading Minecraft font texture to OpenGL");

        ByteBuffer pixels;
        ImInt width = new ImInt();
        ImInt height = new ImInt();
        ImInt bytesPerPixel = new ImInt();

        pixels = ImGui.getIO().getFonts().getTexDataAsRGBA32(width, height, bytesPerPixel);

        if (pixels == null) {
            throw new IllegalStateException("Grimoires: Failed to get font texture data from ImGui.");
        }

        fontTextureId = GlStateManager._genTexture();
        GlStateManager._bindTexture(fontTextureId);
        GlStateManager._texParameter(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        GlStateManager._texParameter(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        GlStateManager._texImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels.asIntBuffer());

        ImGui.getIO().getFonts().setTexID(fontTextureId);

        ImGui.getIO().getFonts().clearTexData();

        GrimoiresMod.LOGGER.info("Finished uploading Minecraft font texture to OpenGL");
    }
}
