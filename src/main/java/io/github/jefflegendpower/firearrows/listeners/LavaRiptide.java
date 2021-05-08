package io.github.jefflegendpower.firearrows.listeners;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import io.github.jefflegendpower.firearrows.FireArrows;
import io.github.jefflegendpower.firearrows.Utils;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.Plugin;

public class LavaRiptide implements Listener {

    private final Plugin plugin = FireArrows.getPlugin(FireArrows.class);
    private boolean riptideState = false;

    @EventHandler
    public void onThrowTrident(ProjectileLaunchEvent event) {

        try {

            if (!(event.getEntity().getShooter() instanceof Player)) {
                return;
            }
            Player player = (Player) event.getEntity().getShooter();

            player.sendMessage(Utils.chat("&2Entity is a player!"));

            if (!(player.getInventory().getItemInMainHand().getItemMeta().hasItemFlag(ItemFlag.HIDE_UNBREAKABLE))) return;

            EntityPlayer entityPlayer = (EntityPlayer) ((CraftPlayer) player).getHandle();
            player.sendMessage(Utils.chat("&2Event Listened!"));

            DataWatcher dataWatcher = entityPlayer.getDataWatcher();
            event.setCancelled(true);
            executeRiptide(dataWatcher);
            riptideState = true;

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void executeRiptide(DataWatcher dataWatcher) {

        dataWatcher.set(DataWatcherRegistry.a.a(7), (byte) 0x04);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            dataWatcher.set(DataWatcherRegistry.a.a(7), (byte) 0x00);
        }, 40L); // amount to wait in ticks , 20 ticks = 1 second
    }

    @EventHandler
    public void onHitWithRiptide() {

    }
}
