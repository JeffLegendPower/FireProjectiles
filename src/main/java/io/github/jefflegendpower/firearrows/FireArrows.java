package io.github.jefflegendpower.firearrows;

import io.github.jefflegendpower.firearrows.Utils.Utils;
import io.github.jefflegendpower.firearrows.items.GUI;
import io.github.jefflegendpower.firearrows.listeners.LavaRiptide;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class FireArrows extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(new GUI(), this);
        this.getServer().getPluginManager().registerEvents(new LavaRiptide(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("fireitems") && sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("FireItems.admin") || !player.hasPermission("*") || !player.isOp()){
                player.sendMessage(Utils.chat("&cYou do not have permission to execute that command."));
                return false;
            }
            new GUI().openInventory(player);
        }
        return true;
    }
}
