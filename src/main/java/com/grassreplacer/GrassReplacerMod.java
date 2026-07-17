package com.grassreplacer;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import java.io.*;
import java.nio.file.*;

public class GrassReplacerMod implements ModInitializer {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("grassreplacer.json");
    private static boolean realisticMode = false;
    private static KeyBinding toggleKey;
    
    @Override
    public void onInitialize() {
        loadConfig();
        
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.grassreplacer.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "category.grassreplacer.main"));
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                realisticMode = !realisticMode;
                saveConfig();
                if (client.player != null) {
                    client.player.sendMessage(Text.literal(realisticMode ? "§aGrama Realista ATIVADA" : "§7Grama Padrao"), true);
                }
                client.worldRenderer.reload();
            }
        });
    }
    
    private void loadConfig() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader r = Files.newBufferedReader(CONFIG_PATH)) {
                realisticMode = new Gson().fromJson(r, Config.class).realisticMode;
            } catch (IOException e) {}
        }
    }
    
    private void saveConfig() {
        try {
            Files.writeString(CONFIG_PATH, new Gson().toJson(new Config()));
        } catch (IOException e) {}
    }
    
    private class Config {
        boolean realisticMode = GrassReplacerMod.this.realisticMode;
    }
    
    public static boolean isRealisticMode() { return realisticMode; }
}
