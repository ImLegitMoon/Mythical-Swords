package com.mythical.mythicalswords.listener;

import com.mythical.mythicalswords.MythicalSwordsPlugin;
import com.mythical.mythicalswords.data.CooldownManager;
import com.mythical.mythicalswords.sword.ItemManager;
import com.mythical.mythicalswords.sword.SwordType;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.UUID;

public class AbilityListener implements Listener {

    private static final UUID STRENGTH_MODIFIER_ID = UUID.fromString("f7221b1f-0f56-4c7c-99f3-83cf1c9d3e35");

    private final MythicalSwordsPlugin plugin;
    private final ItemManager itemManager;
    private final CooldownManager cooldowns;

    public AbilityListener(MythicalSwordsPlugin plugin) {
        this.plugin = plugin;
        this.itemManager = plugin.getItemManager();
        this.cooldowns = plugin.getCooldownManager();
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;
        SwordType type = itemManager.getType(player.getInventory().getItemInMainHand())
                .or(() -> itemManager.getType(player.getInventory().getItemInOffHand()))
                .orElse(null);
        if (type == null) return;
        if (type == SwordType.FEATHER || type == SwordType.HEART) return;

        event.setCancelled(true); // prevent swapping while triggering ability

        if (cooldowns.isOnCooldown(player.getUniqueId(), type.getId())) {
            long remaining = cooldowns.getRemaining(player.getUniqueId(), type.getId()) / 1000;
            player.sendMessage("Ability on cooldown: " + remaining + "s left.");
            return;
        }

        switch (type) {
            case RESISTANCE -> applyResistance(player);
            case STRENGTH -> applyStrength(player);
            case SPEED -> applySpeed(player);
            case TP -> applyTeleport(player);
            case FROST -> applyFrost(player);
            case FLAME -> applyFlame(player);
            default -> { }
        }

        cooldowns.setCooldown(player.getUniqueId(), type.getId(), SwordType.COOLDOWN_MILLIS);
    }

    private void applyResistance(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, SwordType.ABILITY_DURATION_TICKS, 4, false, false, true));
        player.sendMessage("Resistance V applied for 10s.");
    }

    private void applySpeed(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, SwordType.ABILITY_DURATION_TICKS, 2, false, false, true));
        player.sendMessage("Speed III applied for 10s.");
    }

    private void applyStrength(Player player) {
        AttributeModifier modifier = new AttributeModifier(
                STRENGTH_MODIFIER_ID,
                "mythicalswords_strength",
                4.0,
                AttributeModifier.Operation.ADD_NUMBER);
        var attr = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (attr != null) {
            AttributeModifier existing = attr.getModifier(STRENGTH_MODIFIER_ID);
            if (existing != null) attr.removeModifier(existing);
            attr.addTransientModifier(modifier);
            player.sendMessage("+4 attack damage for 10s.");
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                AttributeModifier current = attr.getModifier(STRENGTH_MODIFIER_ID);
                if (current != null) attr.removeModifier(current);
                player.sendMessage("Strength bonus expired.");
            }, SwordType.ABILITY_DURATION_TICKS);
        }
    }

    private void applyTeleport(Player player) {
        Location eye = player.getEyeLocation();
        World world = player.getWorld();
        RayTraceResult result = world.rayTraceBlocks(eye, eye.getDirection(), 15.0, FluidCollisionMode.NEVER, true);
        Location target;
        if (result != null && result.getHitPosition() != null) {
            Vector hit = result.getHitPosition();
            Vector dir = eye.getDirection().normalize();
            target = hit.subtract(dir.multiply(0.5)).toLocation(world);
        } else {
            target = eye.add(eye.getDirection().normalize().multiply(15.0));
        }
        target.setY(Math.floor(target.getY()));
        if (!isSafe(target)) {
            player.sendMessage("No safe spot to teleport.");
            return;
        }
        player.teleport(target);
        player.sendMessage("Teleported up to 15 blocks forward.");
    }

    private boolean isSafe(Location loc) {
        Location feet = loc.clone();
        Location head = loc.clone().add(0, 1, 0);
        return feet.getBlock().isPassable() && head.getBlock().isPassable();
    }

    private void applyFrost(Player player) {
        Entity target = player.getTargetEntity(15);
        if (target instanceof LivingEntity living && !target.equals(player)) {
            living.setFreezeTicks(SwordType.ABILITY_DURATION_TICKS);
            living.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, SwordType.ABILITY_DURATION_TICKS, 6, false, false, true));
            player.sendMessage("Frozen target for 10s.");
        } else {
            player.sendMessage("No target in sight to freeze.");
        }
    }

    private void applyFlame(Player player) {
        Entity target = player.getTargetEntity(15);
        if (target instanceof LivingEntity living && !target.equals(player)) {
            living.setFireTicks(SwordType.ABILITY_DURATION_TICKS);
            player.sendMessage("Set target ablaze for 10s.");
        } else {
            player.sendMessage("No target in sight to burn.");
        }
    }
}
