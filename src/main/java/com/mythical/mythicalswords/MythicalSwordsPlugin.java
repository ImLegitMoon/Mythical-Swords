package com.mythical.mythicalswords;

import com.mythical.mythicalswords.command.MWCommand;
import com.mythical.mythicalswords.data.CooldownManager;
import com.mythical.mythicalswords.data.DataManager;
import com.mythical.mythicalswords.listener.AbilityListener;
import com.mythical.mythicalswords.listener.CraftListener;
import com.mythical.mythicalswords.listener.PassiveHelper;
import com.mythical.mythicalswords.listener.PassiveListener;
import com.mythical.mythicalswords.sword.ItemManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MythicalSwordsPlugin extends JavaPlugin {

    private static MythicalSwordsPlugin instance;
    private ItemManager itemManager;
    private DataManager dataManager;
    private CooldownManager cooldownManager;
    private PassiveHelper passiveHelper;

    public static MythicalSwordsPlugin getInstance() {
        return instance;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public PassiveHelper getPassiveHelper() {
        return passiveHelper;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.dataManager = new DataManager(this);
        this.dataManager.load();

        this.cooldownManager = new CooldownManager();
        this.itemManager = new ItemManager(this);
        this.passiveHelper = new PassiveHelper(this.itemManager);
        this.itemManager.registerRecipes();

        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new CraftListener(this), this);
        getServer().getPluginManager().registerEvents(new PassiveListener(this), this);

        MWCommand command = new MWCommand(this);
        getCommand("mw").setExecutor(command);
        getCommand("mw").setTabCompleter(command);
    }

    @Override
    public void onDisable() {
        dataManager.save();
    }
}
