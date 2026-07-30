package com.bowboost.helper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

/**
 * Draws a small indicator just below the crosshair whenever the player is
 * holding a bow (main hand or off hand) — drawn or not. Green = pitch is
 * within the configured optimal range, gray = holding a bow but outside
 * the range.
 * Draws nothing at all if the mod is disabled or the player isn't holding a bow.
 */
public class HudRenderer {

    private static final int COLOR_GOOD = 0xFF3DDC53;   // green
    private static final int COLOR_BAD = 0xFFB0B0B0;    // gray
    private static final int COLOR_TEXT = 0xFFFFFFFF;   // white

    private final Config config;

    public HudRenderer(Config config) {
        this.config = config;
    }

    public void render(DrawContext context, float tickDelta) {
        if (!config.enabled) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) {
            return;
        }

        if (!isHoldingBow(client)) {
            return;
        }

        float pitch = client.player.getPitch(tickDelta);
        boolean inRange = config.isInOptimalRange(pitch);
        int color = inRange ? COLOR_GOOD : COLOR_BAD;

        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        // Small square indicator just below the crosshair.
        int size = 4;
        int gapBelowCrosshair = 12;
        int x1 = centerX - size / 2;
        int y1 = centerY + gapBelowCrosshair;
        int x2 = x1 + size;
        int y2 = y1 + size;

        MatrixStack matrices = context.getMatrices();
        matrices.push();
        context.fill(x1, y1, x2, y2, color);
        matrices.pop();

        if (config.showPitchValue) {
            String pitchLabel = String.format("%.1f\u00B0", pitch);
            Text label = Text.literal(pitchLabel);
            int textWidth = client.textRenderer.getWidth(label);
            int textX = centerX - textWidth / 2;
            int textY = y2 + 3;
            context.drawTextWithShadow(client.textRenderer, label, textX, textY, COLOR_TEXT);
        }
    }

    private boolean isHoldingBow(MinecraftClient client) {
        ItemStack mainHand = client.player.getStackInHand(Hand.MAIN_HAND);
        ItemStack offHand = client.player.getStackInHand(Hand.OFF_HAND);
        return isBowLike(mainHand) || isBowLike(offHand);
    }

    private boolean isBowLike(ItemStack stack) {
        return stack.isOf(Items.BOW) || stack.isOf(Items.CROSSBOW);
    }
}
