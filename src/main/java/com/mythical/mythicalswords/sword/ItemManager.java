package com.mythical.mythicalswords.sword;

import com.mythical.mythicalswords.MythicalSwordsPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ItemManager {

    private final MythicalSwordsPlugin plugin;
    private final NamespacedKey swordKey;
    private final Map<SwordType, ItemStack> cache = new EnumMap<>(SwordType.class);

    public ItemManager(MythicalSwordsPlugin plugin) {
        this.plugin = plugin;
        this.swordKey = new NamespacedKey(plugin, "sword_type");
        for (SwordType type : SwordType.values()) {
            cache.put(type, createSword(type));
        }
    }

    public NamespacedKey getSwordKey() {
        return swordKey;
    }

    public ItemStack getSword(SwordType type) {
        return cache.get(type).clone();
    }

    public Optional<SwordType> getType(ItemStack stack) {
        if (stack == null || stack.getType() != Material.NETHERITE_SWORD) return Optional.empty();
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return Optional.empty();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String id = pdc.get(swordKey, PersistentDataType.STRING);
        if (id == null) return Optional.empty();
        return SwordType.fromName(id);
    }

    public boolean isSword(ItemStack stack, SwordType type) {
        return getType(stack).map(t -> t == type).orElse(false);
    }

    private ItemStack createSword(SwordType type) {
        ItemStack item = new ItemStack(type.getBaseMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(type.getDisplay(), type.getColor()).decoration(TextDecoration.ITALIC, false));
        meta.lore(List.of(
                Component.text(type.getLore(), NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text("Unique: one per player", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)
        ));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        meta.getPersistentDataContainer().set(swordKey, PersistentDataType.STRING, type.getId());
        item.setItemMeta(meta);
        return item;
    }

    public void registerRecipes() {
        for (SwordType type : SwordType.values()) {
            NamespacedKey key = new NamespacedKey(plugin, type.getId() + "_recipe");
            ShapedRecipe recipe = new ShapedRecipe(key, getSword(type));
            switch (type) {
                case RESISTANCE -> {
                    recipe.shape("TAT", "IDU", "TAT");
                    recipe.setIngredient('T', Material.TURTLE_HELMET);
                    recipe.setIngredient('A', Material.ENCHANTED_GOLDEN_APPLE);
                    recipe.setIngredient('I', Material.NETHERITE_INGOT);
                    recipe.setIngredient('D', Material.DIAMOND_SWORD);
                    recipe.setIngredient('U', Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
                }
                case STRENGTH -> {
                    recipe.shape("BRB", "IDU", "BRB");
                    recipe.setIngredient('B', Material.BLAZE_ROD);
                    recipe.setIngredient('R', Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE);
                    recipe.setIngredient('I', Material.NETHERITE_INGOT);
                    recipe.setIngredient('D', Material.DIAMOND_SWORD);
                    recipe.setIngredient('U', Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
                }
                case SPEED -> {
                    recipe.shape("TFT", "IDU", "TFT");
                    recipe.setIngredient('T', Material.TRIDENT);
                    recipe.setIngredient('F', Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE);
                    recipe.setIngredient('I', Material.NETHERITE_INGOT);
                    recipe.setIngredient('D', Material.DIAMOND_SWORD);
                    recipe.setIngredient('U', Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
                }
                case TP -> {
                    recipe.shape("EGJ", "IDU", "EHJ");
                    recipe.setIngredient('E', Material.ENDER_EYE);
                    recipe.setIngredient('G', Material.DRAGON_EGG);
                    recipe.setIngredient('J', Material.DRAGON_HEAD);
                    recipe.setIngredient('I', Material.NETHERITE_INGOT);
                    recipe.setIngredient('D', Material.DIAMOND_SWORD);
                    recipe.setIngredient('U', Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
                }
                case FROST -> {
                    recipe.shape("FHF", "IDU", "FHF");
                    recipe.setIngredient('F', Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE);
                    recipe.setIngredient('H', Material.HEART_OF_THE_SEA);
                    recipe.setIngredient('I', Material.NETHERITE_INGOT);
                    recipe.setIngredient('D', Material.DIAMOND_SWORD);
                    recipe.setIngredient('U', Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
                }
                case FLAME -> {
                    recipe.shape("SPS", "IDU", "SNS");
                    recipe.setIngredient('S', Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE);
                    recipe.setIngredient('P', Material.MUSIC_DISC_PIGSTEP);
                    recipe.setIngredient('I', Material.NETHERITE_INGOT);
                    recipe.setIngredient('D', Material.DIAMOND_SWORD);
                    recipe.setIngredient('U', Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
                    recipe.setIngredient('N', Material.NETHER_STAR);
                }
                case FEATHER -> {
                    recipe.shape("BWB", "IDU", "BHB");
                    recipe.setIngredient('B', Material.BREEZE_ROD);
                    recipe.setIngredient('W', Material.WITHER_ROSE);
                    recipe.setIngredient('I', Material.NETHERITE_INGOT);
                    recipe.setIngredient('D', Material.DIAMOND_SWORD);
                    recipe.setIngredient('U', Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
                    recipe.setIngredient('H', Material.HEAVY_CORE);
                }
                case HEART -> {
                    recipe.shape("ABA", "IDU", "BAA");
                    recipe.setIngredient('A', Material.ENCHANTED_GOLDEN_APPLE);
                    recipe.setIngredient('B', Material.BEETROOT);
                    recipe.setIngredient('I', Material.NETHERITE_INGOT);
                    recipe.setIngredient('D', Material.DIAMOND_SWORD);
                    recipe.setIngredient('U', Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
                }
            }
            plugin.getServer().addRecipe(recipe);
        }
    }
}
