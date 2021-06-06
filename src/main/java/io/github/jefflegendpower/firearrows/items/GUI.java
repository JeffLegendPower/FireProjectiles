package io.github.jefflegendpower.firearrows.items;

import io.github.jefflegendpower.firearrows.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUI implements Listener {


    public Items items = new Items();
    private final Inventory inv;
    public GUI() {
        inv = Bukkit.createInventory(null, 6*9, Utils.chat("&4FireProjectiles"));

        // Put the items into the inventory
        initializeItems();
    }
    // You can call this whenever you want to put the items in
    public void initializeItems() {
//        inv.addItem(items.Wither_sword());
//        inv.addItem(items.Archer_Helmet());
//        inv.addItem(items.Archer_Chestplate());
//        inv.addItem(items.Archer_Leggings());
//        inv.addItem(items.Archer_Boots());
//        inv.addItem(items.Flimsy_diamond_pickaxe());
//        inv.addItem(items.Flimsy_golden_pickaxe());
//        inv.addItem(items.Flimsy_iron_pickaxe());
//        inv.addItem(items.Flimsy_stone_pickaxe());
//        inv.addItem(items.Flimsy_wooden_pickaxe());
//        inv.addItem(items.Mysterious_ocarina());
        inv.addItem(items.lavaTrident());
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != inv) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        final Player p = (Player) e.getWhoClicked();

        // Using slots click is a best option for your inventory click's
        p.getInventory().addItem(e.getCurrentItem());
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == inv) {
            e.setCancelled(true);
        }
    }
}
