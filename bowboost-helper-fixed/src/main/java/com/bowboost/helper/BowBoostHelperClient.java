package com.bowboost.helper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/**
 * Bow Boost Helper - a lightweight, client-only practice aid.
 *
 * Shows a small crosshair indicator while drawing a bow that turns green
 * when your pitch (look angle) is within a configurable "optimal" range
 * for bow-boosting practice. Purely visual, no gameplay changes.
 */
public class BowBoostHelperClient implements ClientModInitializer {

    private Config config;
    private HudRenderer hudRenderer;
    private KeyBinding toggleKeyBinding;

    @Override
    public void onInitializeClient() {
        config = Config.load();
        hudRenderer = new HudRenderer(config);

        toggleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bowboosthelper.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "key.categories.bowboosthelper"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
        HudRenderCallback.EVENT.register(this::onHudRender);
    }

    private void onClientTick(MinecraftClient client) {
        while (toggleKeyBinding.wasPressed()) {
            config.enabled = !config.enabled;
            config.save();

            if (client.player != null) {
                String stateMsg = config.enabled
                        ? "Bow Boost Helper: enabled"
                        : "Bow Boost Helper: disabled";
                client.player.sendMessage(Text.literal(stateMsg), true); // action bar, not chat spam
            }
        }
    }

    private void onHudRender(net.minecraft.client.gui.DrawContext drawContext, net.minecraft.client.render.RenderTickCounter tickCounter) {
        hudRenderer.render(drawContext, tickCounter.getTickDelta(true));
    }
}
