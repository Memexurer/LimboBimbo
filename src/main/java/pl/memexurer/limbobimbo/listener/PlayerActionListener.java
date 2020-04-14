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
import pl.memexurer.limbobimbo.config.impl.PluginConfiguration;
import pl.memexurer.limbobimbo.utils.ChatUtil;

import java.util.stream.Collectors;

public class PlayerActionListener implements Listener {
    private PluginConfiguration configuration;

    public PlayerActionListener(PluginConfiguration configuration) {
        this.configuration = configuration;
    }

    @EventHandler
    public void playerJoinHandler(PlayerJoinEvent e) {
        e.getPlayer().setGameMode(GameMode.valueOf(configuration.DEFAULT_GAMEMODE));
        e.getPlayer().getInventory().clear();
        e.getPlayer().getInventory().setItem(4, configuration.QUEUE_JOIN_ITEM);
        e.setJoinMessage("");
    }

    @EventHandler
    public void playerLoginHandler(PlayerLoginEvent e) {
        if (!e.getAddress().toString().split(":")[0].substring(1).equals(configuration.BUNGEE_IP))
            e.disallow(PlayerLoginEvent.Result.KICK_BANNED, configuration.BUNGEE_MESSAGE.stream().map(ChatUtil::fixColor).collect(Collectors.joining("\n")));
    }

    @EventHandler
    public void playerLeaveHandler(PlayerQuitEvent e) {
        e.setQuitMessage("");
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

        if (e.getItem().isSimilar(configuration.QUEUE_JOIN_ITEM)) {
            LimboBimboPlugin.getPluginInstance().getQueueData().addPlayer(e.getPlayer());
            e.getPlayer().getInventory().clear();
            e.getPlayer().sendMessage(ChatColor.GREEN + "Zostales dodany do kolejki!");
        }
    }

    @EventHandler
    public void playerMoveHandler(PlayerMoveEvent e) {
        if (e.getTo().getY() < configuration.SPAWN_TELEPORT_Y) {
            e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
        }
    }
}
