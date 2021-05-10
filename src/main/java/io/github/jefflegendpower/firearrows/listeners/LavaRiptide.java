package io.github.jefflegendpower.firearrows.listeners;

import io.github.jefflegendpower.firearrows.FireArrows;
import io.github.jefflegendpower.firearrows.Utils.Ray;
import io.github.jefflegendpower.firearrows.Utils.Utils;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public class LavaRiptide implements Listener {

    private final Plugin plugin = FireArrows.getPlugin(FireArrows.class);

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
            setRiptideState(dataWatcher);
            damageNearbyPlayers(player);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void setRiptideState(DataWatcher dataWatcher) {

        dataWatcher.set(DataWatcherRegistry.a.a(7), (byte) 0x04);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            dataWatcher.set(DataWatcherRegistry.a.a(7), (byte) 0x00);
        }, 40L); // amount to wait in ticks, 20 ticks = 1 second
    }
    private void damageNearbyPlayers(Player player) {

        Vector direction = player.getEyeLocation().getDirection();
        Location endPoint = player.getEyeLocation().add(direction.multiply(3));

        double playerLocX = player.getEyeLocation().getX();
        double playerLocY = player.getEyeLocation().getY();
        double playerLocZ = player.getEyeLocation().getZ();
        double endPointLocX = endPoint.getX();
        double endPointLocY = endPoint.getY();
        double endPointLocZ = endPoint.getZ();

        Ray ray = new Ray(playerLocX, playerLocY, playerLocZ,endPointLocX, endPointLocY, endPointLocZ);

        List<Entity> nearbyEntities = player.getNearbyEntities(5, 5, 5);
        for (Entity entity : nearbyEntities) {
            System.out.println("Entity detected!");
            ray.entitySet.add(entity);
        }
        ray.removeAllNonColliding();
        ray.entitySet.remove(player);

        Set<Entity> entitySet = ray.getEntities();
        for (Entity entity : entitySet) {
            System.out.println("Entity Caught!");
            ((Damageable) entity).damage(1);
            entity.setGlowing(true);

        }
    }
}
