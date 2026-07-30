package com.bowboost.helper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class BowBoostHelperClient implements ClientModInitializer {

    public static final String MOD_ID = "bowboosthelper";

    private static KeyBinding toggleKey;
    private static HudRenderer hudRenderer;
    private static Config config;

    @Override
    public void onInitializeClient() {
        config = Config.load();

        toggleKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.bowboosthelper.toggle",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_B,
                        KeyBinding.Category.create(
                                Identifier.of(MOD_ID, "key.categories.bowboosthelper")
                        )
                )
        );

        hudRenderer = new HudRenderer(config);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                config.setEnabled(!config.isEnabled());
                config.save();
            }
        });
    }

    public static HudRenderer getHudRenderer() {
        return hudRenderer;
    }

    public static Config getConfig() {
        return config;
    }
}
