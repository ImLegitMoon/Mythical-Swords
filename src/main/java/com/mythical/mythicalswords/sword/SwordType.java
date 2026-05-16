package com.mythical.mythicalswords.sword;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public enum SwordType {
    RESISTANCE("resistance", "Resistance Sword", "Shift+Offhand: Resistance V for 10s (60s cooldown)", TextColor.color(0x4caf50)),
    STRENGTH("strength", "Strength Sword", "Shift+Offhand: +4 attack damage for 10s (60s cooldown)", TextColor.color(0xe65100)),
    SPEED("speed", "Speed Sword", "Shift+Offhand: Speed III for 10s (60s cooldown)", TextColor.color(0x03a9f4)),
    TP("tp", "TP Sword", "Shift+Offhand: Teleport up to 15 blocks forward (60s cooldown)", TextColor.color(0x9c27b0)),
    FROST("frost", "Frost Sword", "Shift+Offhand: Freeze target you're looking at for 10s (60s cooldown)", TextColor.color(0x90caf9)),
    FLAME("flame", "Flame Sword", "Shift+Offhand: Burn target you're looking at for 10s (60s cooldown)", TextColor.color(0xf44336)),
    FEATHER("feather", "Feather Sword", "Passive: No fall damage while in inventory", TextColor.color(0xffc107)),
    HEART("heart", "Heart Sword", "Passive: +15 hearts permanently when obtained", TextColor.color(0xff4081));

    public static final int ABILITY_DURATION_TICKS = 200; // 10 seconds
    public static final long COOLDOWN_MILLIS = 60_000L;   // 60 seconds

    private final String id;
    private final String display;
    private final String lore;
    private final TextColor color;

    SwordType(String id, String display, String lore, TextColor color) {
        this.id = id;
        this.display = display;
        this.lore = lore;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public String getLore() {
        return lore;
    }

    public TextColor getColor() {
        return color;
    }

    public Material getBaseMaterial() {
        return Material.NETHERITE_SWORD;
    }

    public static java.util.Optional<SwordType> fromName(String input) {
        for (SwordType type : values()) {
            if (type.id.equalsIgnoreCase(input)
                    || type.display.equalsIgnoreCase(input)
                    || type.display.replace(" Sword", "").equalsIgnoreCase(input)) {
                return java.util.Optional.of(type);
            }
        }
        return java.util.Optional.empty();
    }
}
