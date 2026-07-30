# Bow Boost Helper

A tiny, client-only Fabric mod for practicing bow-boost pitch timing. It draws
nothing at all unless you're holding a bow or crossbow (in either hand —
doesn't matter if you're drawing it or not), in which case a small square
appears just below your crosshair:

- **Green** — your pitch is inside the configured optimal range
- **Gray** — you're holding a bow but outside the range
- (nothing) — not holding a bow, or the mod is toggled off

It also shows the numeric pitch value under the indicator so you can see
exactly what angle you're at.

## Requirements

- Minecraft 1.21.11
- Fabric Loader 0.16.x+
- Fabric API (matching build for 1.21.11)
- Java 21

**Before your first build**, open `gradle.properties` and confirm the
`yarn_mappings`, `loader_version`, and `fabric_version` values against
https://fabricmc.net/develop/ — these build numbers move fast and the ones
in this template are a starting point, not guaranteed-current.

## Getting a working jar (any launcher)

The built jar isn't tied to a specific launcher or mod manager. Vanilla
Minecraft launcher, Modrinth App, CurseForge App, Prism — all of them work
the same way: install **Fabric Loader** for 1.21.11, put **Fabric API**
and **this mod's jar** in that instance's `mods` folder, done.

If you don't want to set up a local build environment, this repo includes
a GitHub Actions workflow (`.github/workflows/build.yml`) that builds the
jar on every push. Push this project to a GitHub repo, check the **Actions**
tab, and download the `bowboost-helper-jar` artifact once the run finishes
— that's the file that goes in your `mods` folder.

## Building

```
./gradlew build
```

The output jar will be in `build/libs/`. Drop it in your `.minecraft/mods`
folder alongside a matching Fabric API jar.

## Config

On first launch, a config file is created at:

```
.minecraft/config/bowboosthelper.json
```

```json
{
  "minPitch": -55.0,
  "maxPitch": -35.0,
  "enabled": true,
  "showPitchValue": true
}
```

**Pitch convention reminder:** in Minecraft, pitch is *negative* when
looking up and *positive* when looking down (straight up = -90, straight
down = 90, horizon = 0). So the default range means "between 35 and 55
degrees above the horizon." Edit `minPitch`/`maxPitch` to whatever range
you've found actually works for your boosting technique, then restart the
game (or just re-toggle the mod) to reload.

## Keybind

- Default: **B** — toggles the whole mod on/off (shown briefly in the
  action bar). Rebind it in Options > Controls > Bow Boost Helper like
  any other keybind.

## Project layout

```
bowboost-helper/
├── build.gradle
├── gradle.properties
├── settings.gradle
├── README.md
└── src/main/
    ├── resources/
    │   ├── fabric.mod.json
    │   └── assets/bowboosthelper/lang/en_us.json
    └── java/com/bowboost/helper/
        ├── BowBoostHelperClient.java   (entrypoint, keybind + tick + HUD wiring)
        ├── Config.java                 (JSON load/save, range check)
        └── HudRenderer.java            (draws the crosshair indicator)
```

This mod is intentionally scoped to just this one feature — no server
component, no other HUD elements, no gameplay changes.
