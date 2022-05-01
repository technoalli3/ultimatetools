package com.alli.ultimatetool;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class UltimateToolClient implements ClientModInitializer {

    private static KeyBinding toggleKeyBinding;
    private static KeyBinding shearsKeyBinding;
    public static boolean toggleShovelAndHoe = false;
    public static boolean toggleShears = false;


    @Override
    public void onInitializeClient() {
        toggleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ultimatetool.toggle", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_O, // The keycode of the key
                "category.ultimatetool.tool" // The translation key of the keybinding's category.
        ));
        shearsKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ultimatetool.shears", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_SEMICOLON, // The keycode of the key
                "category.ultimatetool.tool" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(toggleKeyBinding.wasPressed()) {//toggle key
                toggleShovelAndHoe = !toggleShovelAndHoe;
                if(toggleShovelAndHoe) {
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.of("Set to hoe mode"), false);

                } else {
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.of("Set to shovel mode"), false);
                }
            }//toggle key
            if(shearsKeyBinding.wasPressed()) {
                toggleShears = !toggleShears;
                if(toggleShears) {
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.of("Shears enabled"), false);
                } else {
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.of("Shears disabled"), false);
                }
            }
        });
    }
}
