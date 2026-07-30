package com.bowboost.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simple JSON-backed config for Bow Boost Helper.
 *
 * Reminder on pitch: in Minecraft, pitch is negative when looking UP and
 * positive when looking DOWN (straight up = -90, straight down = 90).
 * So "minPitch = -55, maxPitch = -35" means "between 35 and 55 degrees
 * above the horizon."
 */
public class Config {

    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("bowboosthelper.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /** Lower bound (more negative / higher up) of the optimal pitch range. */
    public float minPitch = -55.0f;

    /** Upper bound (less negative / closer to horizon) of the optimal pitch range. */
    public float maxPitch = -35.0f;

    /** Whether the HUD indicator is active. Toggled with the keybind. */
    public boolean enabled = true;

    /** Show the numeric pitch value next to the indicator. */
    public boolean showPitchValue = true;

    public static Config load() {
        if (!Files.exists(CONFIG_PATH)) {
            Config defaults = new Config();
            defaults.save();
            return defaults;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
            Config loaded = GSON.fromJson(reader, Config.class);
            if (loaded == null) {
                loaded = new Config();
            }
            // Guard against a hand-edited config with min/max swapped.
            if (loaded.minPitch > loaded.maxPitch) {
                float tmp = loaded.minPitch;
                loaded.minPitch = loaded.maxPitch;
                loaded.maxPitch = tmp;
            }
            return loaded;
        } catch (IOException e) {
            System.err.println("[BowBoostHelper] Failed to read config, using defaults: " + e.getMessage());
            return new Config();
        }
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH, StandardCharsets.UTF_8)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            System.err.println("[BowBoostHelper] Failed to save config: " + e.getMessage());
        }
    }

    public boolean isInOptimalRange(float pitch) {
        return pitch >= minPitch && pitch <= maxPitch;
    }
}
