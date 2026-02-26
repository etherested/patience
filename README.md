# Patience

[![NeoForge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact-minimal/supported/neoforge_vector.svg)](https://neoforged.net/)
[![Forge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact-minimal/supported/forge_vector.svg)](https://minecraftforge.net/)
[![Fabric](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact-minimal/supported/fabric_vector.svg)](https://fabricmc.net/)
[![Quilt](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact-minimal/supported/quilt_vector.svg)](https://quiltmc.org/)
[![Modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact-minimal/available/modrinth_vector.svg)](https://modrinth.com/mod/patience)
[![CurseForge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact-minimal/available/curseforge_vector.svg)](https://www.curseforge.com/minecraft/mc-mods/patience)
[![GitHub](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/social/github-singular_vector.svg)](https://github.com/etherested/patience)
[![Discord](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/social/discord-singular_vector.svg)](https://discord.com/users/1281644937536077832)

Adds crafting time to your workstations — clicking the output slot starts a timed process instead of completing the craft instantly. Progress is shown as a visual overlay with sounds and feedback. Fully server-authoritative: the config is synced to all clients on join and reload.

## ⚙️ Features

**Crafting Delay** — Base time scales with the number of ingredients and is modified by a stack of multipliers (global, per-container, per-ingredient, per-output, per-recipe). Crafting is blocked while moving. Creative mode bypasses the delay entirely.

**Experience Scaling** — Crafting speed increases with XP level through a custom `patience:crafting_speed` attribute. The attribute is exposed to other mods for integration.

**Progress Decay** — When enabled, moving during a craft causes progress to drain instead of cancelling outright. Leftover progress from an interrupted craft also decays over time. When decay is disabled, any movement immediately cancels the craft and resets progress.

**Minigame** — An optional skill check that can trigger mid-craft. A random section of the progress overlay is designated as the success zone. When progress reaches that zone, the overlay turns yellow — clicking the output slot during this completes the craft instantly. Missing it applies a time penalty. Clicking the output slot early when no minigame zone is active also applies a penalty. Green indicates success, red indicates failure.

**Hunger** — Each completed craft adds exhaustion. When hunger drops below a threshold, crafting speed is penalized.

**Sounds** — Each workstation has a unique looping crafting sound and a completion sound. Sounds can be overridden at three levels: per container, per item, or per tag.

**Screen Shake** — Optional camera shake during crafting. Disabled by default.

## 🔨 Supported Containers

Ships with preconfigured support for 17 workstations. All can be disabled, customized, or extended via config.

| | Containers |
|---|---|
| **Vanilla** | Player inventory (2×2), Crafting table, Smithing table, Anvil, Grindstone, Stonecutter, Cartography table, Loom |
| **Modded** | [Curios](https://modrinth.com/mod/curios), [Inventorio](https://modrinth.com/mod/inventorio), [Sawmill](https://modrinth.com/mod/universal-sawmill), [Woodworks](https://modrinth.com/mod/woodworks), [Easel Does It](https://modrinth.com/mod/easel-does-it), [Galosphere](https://modrinth.com/mod/galosphere), [Clayworks](https://modrinth.com/mod/clayworks), [Cold Sweat](https://modrinth.com/mod/cold-sweat), [Hexerei](https://modrinth.com/mod/hexerei) |

## 🛠️ Configuration

Config is stored in `patience.json` in the game's config directory. A GUI screen is available when [Cloth Config](https://modrinth.com/mod/cloth-config) is installed (Fabric/Quilt via [Mod Menu](https://modrinth.com/mod/modmenu), NeoForge/Forge via the mods screen). The GUI covers general settings — multiplier maps and container definitions are JSON-only.

<details>
<summary><b>General</b></summary>

| Setting | Default | Description |
|---|---|---|
| `debug` | `false` | Log slot clicks, screen names, crafting state changes, and movement checks to the console |
| `enable_sounds` | `true` | Play crafting and completion sounds |
| `default_crafting_sound` | `patience:crafting` | Sound played during crafting |
| `default_finish_sound` | `patience:finish` | Sound played on completion |
| `default_penalty_sound` | `patience:penalty` | Sound played on penalty click |
| `default_success_sound` | `patience:success` | Sound played on minigame success |
| `global_time_multiplier` | `1.0` | Multiplier applied to all craft times (0.0 - 100.0) |

</details>

<details>
<summary><b>Experience</b></summary>

Speed formula: `(attributeValue × baseSpeed) + (speedPerLevel × min(xpLevel, maxLevelCap))`

| Setting | Default | Description |
|---|---|---|
| `experience_multiplier` | `1.0` | Overall crafting speed multiplier (0.0 - 100.0) |
| `experience_base_speed` | `1.0` | Base speed before XP bonus (0.01 - 100.0) |
| `experience_speed_per_level` | `0.02` | Bonus speed per XP level (0.0 - 10.0) |
| `experience_max_level_cap` | `200` | Levels above this are ignored (0 - 30000) |

</details>

<details>
<summary><b>Decay</b></summary>

| Setting | Default | Description |
|---|---|---|
| `decay_enabled` | `true` | Moving drains progress instead of cancelling the craft |
| `decay_rate` | `2.0` | Progress lost per tick while moving or idle (0.0 - 100.0) |

</details>

<details>
<summary><b>Screen Shake</b></summary>

| Setting | Default | Description |
|---|---|---|
| `screen_shake_enabled` | `false` | Camera shake during crafting |
| `screen_shake_intensity` | `0.5` | Shake strength (0.0 - 5.0) |

</details>

<details>
<summary><b>Hunger</b></summary>

| Setting | Default | Description |
|---|---|---|
| `hunger_exhaustion_cost` | `0.1` | Exhaustion added per completed craft (0.0 - 40.0) |
| `hunger_penalty_enabled` | `true` | Slow crafting at low hunger |
| `hunger_threshold` | `6` | Hunger level that triggers the penalty (0 - 20) |
| `hunger_penalty_multiplier` | `0.5` | Speed multiplier when below threshold (0.0 - 10.0) |

</details>

<details>
<summary><b>Minigame</b></summary>

| Setting | Default | Description |
|---|---|---|
| `minigame_enabled` | `true` | Enable the skill-check minigame |
| `minigame_chance` | `0.5` | Probability of triggering per craft (0.0 - 1.0) |
| `minigame_window_width` | `0.15` | Success zone size as a fraction of total progress (0.01 - 0.5) |
| `minigame_penalty_percent` | `0.25` | Progress lost on failure as a fraction of remaining (0.0 - 1.0) |
| `minigame_penalty_cancels_craft` | `true` | Cancel the craft when penalty clicks reduce progress to zero |

</details>

<details>
<summary><b>Multipliers</b></summary>

Multiplier maps allow fine-grained control over craft time. All are `Map<String, Float>` in the JSON. Tag keys use the `#` prefix (e.g., `"#minecraft:planks": 1.5`).

- `by_mod` — scale time by mod namespace (e.g., `"minecraft": 1.0`)
- `by_item` — scale time by item ID (e.g., `"minecraft:stick": 0.5`)
- `by_tag` — scale time by item tag (e.g., `"#minecraft:planks": 1.5`)
- `by_type` — scale time by recipe type (e.g., `"minecraft:crafting": 2.0`)
- `by_recipe` — scale time by specific recipe ID

These are grouped under `ingredient_multipliers`, `output_multipliers`, and `recipe_multipliers` objects in the JSON. Ingredient and output multipliers support `by_mod`, `by_item`, `by_tag`. Recipe multipliers support `by_type`, `by_recipe`.


</details>

<details>
<summary><b>Item Sounds</b></summary>

`item_sounds` is a `Map<String, String>` that overrides the crafting sound per item or tag (e.g., `"minecraft:stick": "patience:crafting"`, `"#minecraft:planks": "patience:sawmill"`).

</details>

<details>
<summary><b>Containers</b></summary>

The `containers` array holds per-workstation configuration. Each entry supports:

| Field | Type | Description |
|---|---|---|
| `enabled` | boolean | Toggle crafting time for this container |
| `screen_class` | string | Full Java class path of the container screen (e.g., `net.minecraft.client.gui.screens.inventory.CraftingScreen`) |
| `recipe_type` | string | Recipe type ID used for recipe multiplier lookup (e.g., `minecraft:crafting`). If omitted, inferred from the menu type |
| `time_multiplier` | float | Container-specific time multiplier |
| `output_slot` | int | Slot index the player clicks to start crafting (where the output preview appears) |
| `result_slot` | int | Slot index where the finished item is placed after completion (`-1` = first available inventory slot) |
| `ingredient_slots` | string | Slot range defining which slots are scanned for ingredients — e.g., `"1-9"`, `"0,2,4"`, `"0-2,4,6-8"` |
| `ingredient_mode` | string | How ingredients contribute to craft time: `"slot"` counts each occupied slot once, `"stack"` multiplies by stack size, `"custom"` uses hardcoded mod-specific compatibility logic |
| `show_overlay` | boolean | Render the progress bar |
| `overlay_texture` | string | Texture path for the overlay |
| `overlay_direction` | string | Fill direction: `"right"`, `"left"`, `"up"`, `"down"` |
| `overlay_x` / `overlay_y` | int | Overlay position offset |
| `overlay_width` / `overlay_height` | int | Overlay dimensions |
| `crafting_sound` | string | Override crafting sound for this container |
| `finish_sound` | string | Override completion sound for this container |

</details>

## 💬 Commands

| Command | Permission | Description |
|---|---|---|
| `/patience reload` | Op (level 2) | Reload config from disk and sync to all connected players |
