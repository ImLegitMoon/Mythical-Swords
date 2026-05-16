package com.mythical.mythicalswords.listener;

import com.mythical.mythicalswords.sword.ItemManager;
import com.mythical.mythicalswords.sword.SwordType;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PassiveHelper {

    private static final UUID HEART_MODIFIER_ID = UUID.fromString("c7be3c77-3aa8-4a2d-a2db-8bc2cd6371d3");
    private static final double HEART_BONUS = 30.0; // 15 hearts (2 health each)

    private final ItemManager itemManager;

    public PassiveHelper(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public void applyHeartBonus(Player player) {
        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth == null) return;
        AttributeModifier existing = maxHealth.getModifier(HEART_MODIFIER_ID);
        if (existing != null) {
            maxHealth.removeModifier(existing);
        }
        AttributeModifier modifier = new AttributeModifier(
                HEART_MODIFIER_ID,
                "mythicalswords_heart_bonus",
                HEART_BONUS,
                AttributeModifier.Operation.ADD_NUMBER);
        maxHealth.addModifier(modifier);
        if (player.getHealth() > maxHealth.getValue()) {
            player.setHealth(maxHealth.getValue());
        }
    }

    public boolean hasSwordInInventory(Player player, SwordType type) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (itemManager.isSword(item, type)) {
                return true;
            }
        }
        return false;
    }
}
