package io.github.jefflegendpower.firearrows.items;


import io.github.jefflegendpower.firearrows.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Items {

    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack lavaTrident() {
        ItemStack lavaTrident = createGuiItem(Material.TRIDENT, Utils.chat("&4Lava Trident"),
                Utils.chat("&4Trident for lava!"));
        ItemMeta meta = lavaTrident.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        lavaTrident.setItemMeta(meta);

        return lavaTrident;
    }
}
