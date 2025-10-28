# Xeenaa Mod Suite

> **Version:** 2025-10-26  
> **Author:** Miguel Perello  
> **Description:** Modular RPG and world-expansion framework for Minecraft (Fabric).

---

## 🧠 Core / Shared Systems

### Xeenaa-Common
Shared utilities, config loading, data registries, and API bridge across Xeenaa mods.

### Xeenaa-UI-Overhaul
Unified modern UI framework for menus, inventories, dialogs, and skill trees.

---

## ⚔️ RPG Core Systems

### Xeenaa-RPG-Stats
Core RPG attributes (HP, damage, crit, dodge, defense, speed...).

### Xeenaa-RPG-Skills
Player skill progression (combat, mining, farming, etc.) based on RPG-Stats.

### Xeenaa-Professions
Adds player & villager professions with special bonuses, crafting recipes, and XP paths (depends on RPG-Skills).

### Xeenaa-Soulbound
Bound upgradable equipment that scales with player level or consumed resources.

### Xeenaa-Souls
Soul drops from mobs; attach to gear for buffs or abilities (requires RPG-Stats).

### Xeenaa-Companions
Adds tameable or summonable companions that level up via RPG-Stats or Souls.

---

## 🧱 World & Faction Systems

### Xeenaa-Villager-Manager
Profession and behavior management for villagers; adds Guard profession.

### Xeenaa-Factions
Create and manage factions; assign towns and villagers to factions; declare wars.

### Xeenaa-Origins
Assigns origins (human, elf, orc, undead...) to players, villagers, and pillagers; affects stats and faction logic.

### Xeenaa-Pillager-Overhaul
Adds diverse pillager variants and raider types (depends on Origins).

### Xeenaa-Structure-Manager
Controls structure spacing and spawn balancing across structure mods.

### Xeenaa-World-Dynamics
Territory spread, corruption, and biome influence from factions.

### Xeenaa-Events
Dynamic world events (raids, meteor impacts, invasions, etc.) tied to factions and Souls.

---

## 🧠 AI & Behavior

### Xeenaa-Mobs-Overhaul
Improves mob pathfinding, coordination, targeting, and difficulty scaling via config.

---

## 🐾 Content Expansions

### Xeenaa-Alex-Mobs
Fabric port of Alex’s Mobs (foundation for new creatures and biomes).

---

## 🧩 Dependency Map

```text
        ┌────────────────────┐
        │   Xeenaa-Common    │
        └────────┬───────────┘
                 │
        ┌────────▼──────────┐
        │ Xeenaa-RPG-Stats  │
        └──────┬────┬──────┘
               │    │
   ┌───────────▼─┐  │
   │ Xeenaa-RPG- │  │
   │   Skills    │  │
   └─────────────┘  │
                    │
          ┌─────────▼─────────┐
          │ Xeenaa-Professions│
          └───────────────────┘
                    │
   ┌────────────────┼────────────────┐
   │                │                │
┌──▼─────────┐ ┌────▼────────┐ ┌────▼─────────┐
│ Xeenaa-    │ │ Xeenaa-     │ │ Xeenaa-      │
│ Soulbound  │ │ Souls       │ │ Companions   │
└────────────┘ └─────────────┘ └──────────────┘

   ┌─────────────────────────────┐
   │ Xeenaa-Factions             │
   └──────┬──────────┬───────────┘
          │          │
  ┌───────▼──────┐   │
  │ Xeenaa-      │   │
  │ Origins      │   │
  └──────┬───────┘   │
         │           │
 ┌───────▼────────┐  │
 │ Xeenaa-Pillager│  │
 │   -Overhaul    │  │
 └────────────────┘  │
                     │
        ┌────────────▼────────────┐
        │ Xeenaa-World-Dynamics   │
        └────────────┬────────────┘
                     │
             ┌───────▼───────┐
             │ Xeenaa-Events │
             └───────────────┘

      ┌────────────────────────┐
      │ Xeenaa-Mobs-Overhaul   │
      └────────────────────────┘

      ┌────────────────────────┐
      │ Xeenaa-Villager-Manager│
      └────────────────────────┘

      ┌────────────────────────┐
      │ Xeenaa-Alex-Mobs       │
      └────────────────────────┘
```

---

### 🗺️ Legend

- 🟩 **Foundations:** `Common`, `RPG-Stats`, `Factions`
- 🟨 **Systems depending on foundations:** `Skills`, `Souls`, `Professions`, `Origins`
- 🟥 **Content & expansions:** `Companions`, `Pillager-Overhaul`, `Events`, `World-Dynamics`, `Alex-Mobs`
