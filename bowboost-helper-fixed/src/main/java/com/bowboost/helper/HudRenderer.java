package com.bowboost.helper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public class HudRenderer {

public void render(DrawContext context, float tickDelta) {
    MinecraftClient client = MinecraftClient.getInstance();

    if (client.player == null) {
        return;
    }

    if (!isHoldingBow(client)) {
        return;
    }

    var matrices = context.getMatrices();

    String pitchLabel = String.format(
            "Pitch: %.1f",
            client.player.getPitch()
    );

    int x = 10;
    int y = 10;

    context.drawText(
            client.textRenderer,
            Text.literal(pitchLabel),
            x,
            y,
            0xFFFFFF,
            true
    );
}

private boolean isHoldingBow(MinecraftClient client) {
    if (client.player == null) {
        return false;
    }

    ItemStack mainHand = client.player.getStackInHand(Hand.MAIN_HAND);
    ItemStack offHand = client.player.getStackInHand(Hand.OFF_HAND);

    return isBowLike(mainHand) || isBowLike(offHand);
}

private boolean isBowLike(ItemStack stack) {
    return stack.isOf(Items.BOW) || stack.isOf(Items.CROSSBOW);
}

}
