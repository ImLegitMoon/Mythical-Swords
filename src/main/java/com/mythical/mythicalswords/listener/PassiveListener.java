package com.mythical.mythicalswords.listener;

import com.mythical.mythicalswords.data.DataManager;
import com.mythical.mythicalswords.sword.SwordType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PassiveListener implements Listener {

    private final DataManager dataManager;
    private final PassiveHelper helper;

    public PassiveListener(com.mythical.mythicalswords.MythicalSwordsPlugin plugin) {
        this.dataManager = plugin.getDataManager();
        this.helper = plugin.getPassiveHelper();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (dataManager.hasObtained(player.getUniqueId(), SwordType.HEART)) {
            helper.applyHeartBonus(player);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (dataManager.hasObtained(player.getUniqueId(), SwordType.HEART)) {
            helper.applyHeartBonus(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onFall(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (helper.hasSwordInInventory(player, SwordType.FEATHER)) {
            event.setCancelled(true);
        }
    }
}
