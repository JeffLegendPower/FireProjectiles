package io.github.jefflegendpower.firearrows.listeners;

import io.github.jefflegendpower.firearrows.FireArrows;
import io.github.jefflegendpower.firearrows.Utils.MathUtils;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutLightUpdate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LavaRiptide implements Listener {

    private final Plugin plugin = FireArrows.getPlugin(FireArrows.class);
    private byte count = 0;

    @EventHandler
    public void onThrowTrident(ProjectileLaunchEvent event) {
        try {

            if (!(event.getEntity().getShooter() instanceof Player)) {
                return;
            }
            Player player = (Player) event.getEntity().getShooter();

            if (!(player.getInventory().getItemInMainHand().getItemMeta().hasItemFlag(ItemFlag.HIDE_UNBREAKABLE))) return;

            EntityPlayer entityPlayer = (EntityPlayer) ((CraftPlayer) player).getHandle();

            DataWatcher dataWatcher = entityPlayer.getDataWatcher();
            event.setCancelled(true);
            setRiptideState(dataWatcher, player);
            damageNearbyEntities(player);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void setRiptideState(DataWatcher dataWatcher, Player player) {

        Location location = new Location(player.getWorld(),
                player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
                player.getLocation().getYaw(), -10);

        Vector direction = location.getDirection();

        dataWatcher.set(DataWatcherRegistry.a.a(7), (byte) 0x04);
        player.setVelocity(direction.normalize().multiply(3));
        System.out.println(direction.normalize().multiply(3));

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            dataWatcher.set(DataWatcherRegistry.a.a(7), (byte) 0x00);
        }, 40L); // amount to wait in ticks, 20 ticks = 1 second

    }
    private void damageNearbyEntities(Player player) {

        List<Entity> nearbyEntities = player.getNearbyEntities(5, 5, 5);
        HashMap<Integer, Boolean> integerBooleanHashMap;
        for (Entity entity : nearbyEntities) {
            CraftEntity craftEntity = (CraftEntity) entity;
            CraftPlayer craftPlayer = (CraftPlayer) player;
            System.out.println("Entity detected!");
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
                if (craftPlayer.getHandle().getBoundingBox().intersects(craftEntity.getHandle().getBoundingBox())) {
                    entity.setGlowing(true); // THIS IS FOR TESTING PURPOSES
                    System.out.println("Entity Caught!");  // THIS IS FOR TESTING PURPOSES
                }
            }, 0L, 5L);
        }

        blockLaunch(player);
    }
    private void blockLaunch(Player player) {

        final int radius = 25;
        final Vector vector = new Vector(0, 1, 0);
        final List<Integer> blockYLocation = new ArrayList<>();
        final List<FallingBlock> fallingBlocks = new ArrayList<>();

        new BukkitRunnable() {
            Location playerLocation = player.getLocation();
            // Gets old location
            int playerX = playerLocation.getBlockX();
            int playerZ = playerLocation.getBlockZ();

            @Override
            public void run() {
                playerLocation = player.getLocation();

                // Check for non-air blocks from under the player
                for (int y = playerLocation.getBlockY() - radius; y < playerLocation.getBlockY(); y++) {
                    Block block = playerLocation.getWorld().getBlockAt(playerLocation.getBlockX(), y, playerLocation.getBlockZ());
                    if (!(block.getType().isSolid() || block.getType() == Material.GRASS)) continue;
                    blockYLocation.add(y);
                }

                // Find nearest block from the list to the player
                int nearestYValue = MathUtils.closest(playerLocation.getBlockY(), blockYLocation);

                Block block = playerLocation.getWorld().getBlockAt(playerLocation.getBlockX(),
                        nearestYValue, playerLocation.getBlockZ());

                // Make sure the player is on a new block
                if (playerX != playerLocation.getBlockX() || playerZ != playerLocation.getBlockZ()) {

                    FallingBlock fallingBlock = playerLocation.getWorld().spawnFallingBlock
                            (block.getLocation().add(0, 1, 0), block.getBlockData());
                    fallingBlocks.add(fallingBlock);
                    fallingBlock.setGravity(false);
                    fallingBlock.setVelocity(vector.normalize());
                }

                // Cancels runnable when 40 ticks (2 seconds) have passed
                if (count == 40) {
                    for (FallingBlock fallingBlock : fallingBlocks)
                        fallingBlock.setGravity(true);
                    cancel();
                }
                // Updating old location from 1 tick ago
                playerX = playerLocation.getBlockX();
                playerZ = playerLocation.getBlockZ();
                count++;
            }
        }.runTaskTimer(plugin, 1, 1);
    }
}
