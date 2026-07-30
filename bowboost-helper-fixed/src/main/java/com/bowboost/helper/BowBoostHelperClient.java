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

public class BowBoostHelperClient implements ClientModInitializer {

private KeyBinding toggleKeyBinding;
private boolean enabled = true;
private HudRenderer hudRenderer;

@Override
public void onInitializeClient() {
    toggleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.bowboosthelper.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            KeyBinding.Category.create("key.categories.bowboosthelper")
    ));

    hudRenderer = new HudRenderer();

    ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);

    HudRenderCallback.EVENT.register(this::onHudRender);
}

private void onClientTick(MinecraftClient client) {
    while (toggleKeyBinding.wasPressed()) {
        enabled = !enabled;

        if (client.player != null) {
            String stateMsg = enabled
                    ? "Bow Boost Helper: ON"
                    : "Bow Boost Helper: OFF";

            client.player.sendMessage(Text.literal(stateMsg), true);
        }
    }
}

private void onHudRender(
        net.minecraft.client.gui.DrawContext drawContext,
        net.minecraft.client.render.RenderTickCounter tickCounter
) {
    if (!enabled) {
        return;
    }

    hudRenderer.render(drawContext, tickCounter.getTickProgress(true));
}

}
