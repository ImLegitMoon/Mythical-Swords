package com.mythical.mythicalswords.data;

import com.mythical.mythicalswords.sword.SwordType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DataManager {

    private final Plugin plugin;
    private final File file;
    private FileConfiguration config;
    private final Map<UUID, Set<SwordType>> obtained = new HashMap<>();

    public DataManager(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "data.yml");
    }

    public void load() {
        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create data.yml: " + e.getMessage());
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                List<String> list = config.getStringList(key);
                Set<SwordType> set = EnumSet.noneOf(SwordType.class);
                for (String name : list) {
                    SwordType.fromName(name).ifPresent(set::add);
                }
                obtained.put(uuid, set);
            } catch (IllegalArgumentException ignored) {
                plugin.getLogger().warning("Skipping invalid UUID in data.yml: " + key);
            }
        }
    }

    public void save() {
        if (config == null) {
            config = new YamlConfiguration();
        }
        for (String key : config.getKeys(false)) {
            config.set(key, null);
        }
        for (Map.Entry<UUID, Set<SwordType>> entry : obtained.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue().stream().map(SwordType::getId).toList());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save data.yml: " + e.getMessage());
        }
    }

    public boolean hasObtained(UUID uuid, SwordType type) {
        return obtained.getOrDefault(uuid, EnumSet.noneOf(SwordType.class)).contains(type);
    }

    public void markObtained(UUID uuid, SwordType type) {
        obtained.computeIfAbsent(uuid, u -> EnumSet.noneOf(SwordType.class)).add(type);
        save();
    }

    public Set<SwordType> getObtained(UUID uuid) {
        return obtained.getOrDefault(uuid, EnumSet.noneOf(SwordType.class));
    }
}
