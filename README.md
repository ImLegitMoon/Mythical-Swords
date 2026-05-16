# MythicalSwords ⚔️

A high-performance, lightweight Minecraft Spigot/Paper plugin built for the **1.20 API** tier. This plugin introduces custom, lore-infused Netherite tier weapons equipped with unique active abilities, passive traits, and tailored crafting matrices.

Built with an emphasis on clean object-oriented design, optimized event handling, and thread-safe data management.

---

## 🚀 Key Architectural Highlights

This project demonstrates robust software design principles, focusing on decoupled logic, efficient resource management, and clean code organization:

* **Modular OOP Design:** Swords are handled abstractly using enum-driven definitions (`SwordType`) and managed via a centralized lifecycle manager (`ItemManager`), making it highly scalable for adding future tiers.
* **Decoupled Event Architecture:** Dedicated, specialized listeners handle specific mechanics (`CraftListener`, `AbilityListener`, `PassiveListener`), keeping execution rapid and avoiding monolithic class clutter.
* **Thread-Safe Cooldown Tracking:** Features an independent `CooldownManager` to handle active ability limits efficiently without causing server thread lag or exploits.
* **Robust Data Management:** Implements a localized `DataManager` layer to seamlessly manage player metrics, item data, and persistent configurations.

---

## 📂 Package & Systems Breakdown

The project codebase is organized into clean, dedicated packages:

### 1. Core (`com.mythical.mythicalswords`)
* **`MythicalSwordsPlugin`**: The primary entry point. Manages the initialization sequence, registers listeners, injects dependency managers, and safely handles server shutdown procedures.

### 2. Item Management (`.sword`)
* **`ItemManager`**: Handles the construction, custom lore generation, NBT tagging, and tracking of the unique weapons.
* **`SwordType`**: An enum or structural class defining the base properties, damage modifiers, and distinct identity of each mythical blade.

### 3. Logic & Listeners (`.listener`)
* **`AbilityListener`**: Detects player interactions (like right-clicks) to trigger high-impact, active combat abilities.
* **`PassiveListener` & `PassiveHelper`**: Monitors ongoing player states or combat exchanges to apply passive traits (e.g., life steal, speed boosts, or elemental damage) seamlessly.
* **`CraftListener`**: Intercepts the workbench recipe matrix to allow for custom, balanced crafting recipes unique to these mythical tiers.

### 4. Data & Optimization (`.data`)
* **`DataManager`**: Oversees localized storage configuration and state persistence.
* **`CooldownManager`**: Tracks and throttles ability triggers, preventing spam and ensuring balanced gameplay.

### 5. Commands (`.command`)
* **`MWCommand`**: The administrative command handler supporting system configuration and item distribution.

---

## 🛠️ Commands & Permissions

The plugin utilizes a secure, permission-guarded command architecture to safely distribute weapons to the player base.

| Command | Description | Usage | Permission Node | Default |
| :--- | :--- | :--- | :--- | :--- |
| `/mw` | Obtain a specific mythical sword once per player. | `/mw <sword>` | `mythicalswords.command` | **OP** |

---

## 💻 Development Stack & Tools
* **Language:** Java 
* **Target Platform:** Spigot / Paper API (**1.20**)
* **Architecture:** Object-Oriented Programming (OOP), Component-Based Event Handling

---
*Note: The core source code for this project is hosted in a private repository to protect custom gameplay assets and proprietary server logic. For architectural inquiries or technical discussion, feel free to reach out.*
