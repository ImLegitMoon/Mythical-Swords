package com.mythical.mythicalswords.command;

import com.mythical.mythicalswords.MythicalSwordsPlugin;
import com.mythical.mythicalswords.sword.SwordType;
import com.mythical.mythicalswords.listener.PassiveHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MWCommand implements CommandExecutor, TabCompleter {

    private final MythicalSwordsPlugin plugin;
    private final PassiveHelper passiveHelper;

    public MWCommand(MythicalSwordsPlugin plugin) {
        this.plugin = plugin;
        this.passiveHelper = plugin.getPassiveHelper();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only.");
            return true;
        }
        if (!player.hasPermission("mythicalswords.command")) {
            player.sendMessage("You do not have permission.");
            return true;
        }
        if (args.length != 1) {
            player.sendMessage("Usage: /" + label + " <sword>");
            return true;
        }
        SwordType type = SwordType.fromName(args[0]).orElse(null);
        if (type == null) {
            player.sendMessage("Unknown sword. Options: resistance, strength, speed, tp, frost, flame, feather, heart");
            return true;
        }
        if (plugin.getDataManager().hasObtained(player.getUniqueId(), type)) {
            player.sendMessage("You already obtained the " + type.getDisplay() + ".");
            return true;
        }
        plugin.getDataManager().markObtained(player.getUniqueId(), type);
        if (type == SwordType.HEART) {
            passiveHelper.applyHeartBonus(player);
        }
        player.getInventory().addItem(plugin.getItemManager().getSword(type));
        Bukkit.broadcastMessage(player.getName() + " has obtained the " + type.getDisplay() + "!");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            String prefix = args[0].toLowerCase(Locale.ROOT);
            for (SwordType type : SwordType.values()) {
                if (type.getId().startsWith(prefix)) {
                    list.add(type.getId());
                }
            }
            return list;
        }
        return List.of();
    }
}
