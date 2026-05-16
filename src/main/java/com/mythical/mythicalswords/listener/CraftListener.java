package com.mythical.mythicalswords.listener;

import com.mythical.mythicalswords.MythicalSwordsPlugin;
import com.mythical.mythicalswords.data.DataManager;
import com.mythical.mythicalswords.sword.ItemManager;
import com.mythical.mythicalswords.sword.SwordType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CraftListener implements Listener {

    private final MythicalSwordsPlugin plugin;
    private final ItemManager itemManager;
    private final DataManager dataManager;
    private final PassiveHelper passiveHelper;

    public CraftListener(MythicalSwordsPlugin plugin) {
        this.plugin = plugin;
        this.itemManager = plugin.getItemManager();
        this.dataManager = plugin.getDataManager();
        this.passiveHelper = plugin.getPassiveHelper();
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        ItemStack result = event.getRecipe() != null ? event.getRecipe().getResult() : null;
        SwordType type = itemManager.getType(result).orElse(null);
        if (type == null) return;

        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (dataManager.hasObtained(player.getUniqueId(), type)) {
            player.sendMessage("You have already crafted the " + type.getDisplay() + ".");
            event.setCancelled(true);
            return;
        }

        // Mark obtained and announce
        dataManager.markObtained(player.getUniqueId(), type);
        if (type == SwordType.HEART) {
            passiveHelper.applyHeartBonus(player);
        }
        Bukkit.broadcastMessage(player.getName() + " has crafted the " + type.getDisplay() + "!");
    }
}
