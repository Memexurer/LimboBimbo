package pl.memexurer.limbobimbo.listener;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.*;
import pl.memexurer.limbobimbo.LimboBimboPlugin;

public class PlayerActionListener implements Listener {
    @EventHandler
    public void playerJoinHandler(PlayerJoinEvent e) {
        e.getPlayer().setGameMode(GameMode.valueOf(LimboBimboPlugin.getPluginInstance().getConfiguration().DEFAULT_GAMEMODE));
        e.getPlayer().getInventory().clear();
        e.getPlayer().getInventory().setItem(4, LimboBimboPlugin.getPluginInstance().getConfiguration().QUEUE_JOIN_ITEM);
        e.setJoinMessage("");
    }

    @EventHandler
    public void playerLeaveHandler(PlayerQuitEvent e) {
        e.setQuitMessage("");
        LimboBimboPlugin.getPluginInstance().getAuthenticationData().deauthenticate(e.getPlayer());
    }

    @EventHandler
    public void blockBreakHandler(BlockBreakEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void blockPlaceHandler(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void damageReceiveHandler(EntityDamageEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void entitySpawn(EntitySpawnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void commandHandler(PlayerCommandPreprocessEvent e) {
        if (!e.getPlayer().isOp()) e.setCancelled(true);
    }

    @EventHandler
    public void chatHandler(AsyncPlayerChatEvent e) {
        if (!e.getPlayer().isOp()) {
            e.getPlayer().sendMessage(ChatColor.RED + "Nie mozna uzywac chatu w lobby!");
            e.setCancelled(true);
        } else {
            e.setFormat(ChatColor.RED + e.getPlayer().getName() + ChatColor.DARK_GRAY + " \u00BB " + ChatColor.GRAY + e.getMessage());
        }
    }

    @EventHandler
    public void dropItemHandler(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void interactHandler(PlayerInteractEvent e) {
        if (e.getItem() == null) return;

        if (e.getItem().isSimilar(LimboBimboPlugin.getPluginInstance().getConfiguration().QUEUE_JOIN_ITEM)) {
            if (!LimboBimboPlugin.getPluginInstance().getAuthenticationData().isAuthenticated(e.getPlayer())) {
                e.getPlayer().sendMessage(ChatColor.AQUA + "Oczekiwanie na autoryzacje...");
                return;
            }

            LimboBimboPlugin.getPluginInstance().getQueueData().addPlayer(e.getPlayer());
            e.getPlayer().getInventory().clear();
            e.getPlayer().sendMessage(ChatColor.GREEN + "Zostales dodany do kolejki!");
        }
    }

    @EventHandler
    public void playerMoveHandler(PlayerMoveEvent e) {
        if (e.getTo().getY() < LimboBimboPlugin.getPluginInstance().getConfiguration().SPAWN_TELEPORT_Y) {
            e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
        }
    }
}
