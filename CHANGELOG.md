# Changelog

## Unreleased

### 🧩 Added

- Penalty for clicking the output slot during crafting, even without a minigame zone active
- `penalty_cancels_craft` option to cancel craft when penalty clicks reduce progress to zero (default: `true`)
- Penalty sound effect on each penalty click (`default_penalty_sound`, defaults to `patience:penalty`)
- Success sound effect on minigame success (`default_success_sound`, defaults to `patience:success`)

### 🛠️ Changed

- Minigame penalty clicks are now repeatable instead of one-shot
- Crafting sounds play as individual beats instead of a continuous loop, synced with hand swings and subtitles
- Penalty clicks stop the crafting sound and freeze progress during the red flash feedback

### 🐞 Fixed

- Anvil crafting sound not playing on 1.20.1 (`block.heavy_core.break` replaced with `block.anvil.use`)

## 2.1.0: 2026-02-24

### 🧩 Added

- Minecraft 1.20.1 support for Forge and Fabric
- Quilt compatibility for Fabric builds

## 2.0.1: 2026-02-24

### 🐞 Fixed

- Crafting delays not applying on Fabric due to vanilla screen class names being remapped to intermediary at runtime

## 2.0.0: 2026-02-23

### 🧩 Added

- NeoForge + Fabric multiloader via Stonecutter
- Crafting delay with multiplier system (ingredient, output, recipe)
- Experience-based speed scaling via `patience:crafting_speed` attribute
- Progress decay on movement
- Skill-check minigame with overlay feedback
- Hunger exhaustion and speed penalty
- Per-workstation sounds with item/tag overrides
- Screen shake (disabled by default)
- 17 preconfigured containers (8 vanilla, 9 modded)
- JSON config with server-to-client sync
- In-game config screen via Cloth Config, `/patience reload` command
- Additional EMI and Hexerei compatibility
