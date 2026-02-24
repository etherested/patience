# Changelog

## Unreleased

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
