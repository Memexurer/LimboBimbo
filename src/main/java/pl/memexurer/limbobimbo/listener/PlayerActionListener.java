package pl.memexurer.limbobimbo.listener;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.memexurer.limbobimbo.LimboBimboPlugin;
import pl.memexurer.limbobimbo.config.impl.PluginConfiguration;
import pl.memexurer.limbobimbo.utils.ChatUtil;

import java.util.stream.Collectors;

public class PlayerActionListener implements Listener {
    private PluginConfiguration configuration;
    private final ServerPacketHandler packetHandler = new ServerPacketHandler();

    public PlayerActionListener(PluginConfiguration configuration) {
        this.configuration = configuration;
    }

    @EventHandler
    public void playerJoinHandler(PlayerJoinEvent e) {
        e.getPlayer().setGameMode(GameMode.valueOf(configuration.DEFAULT_GAMEMODE));
        e.getPlayer().getInventory().clear();
        if (configuration.ITEM_ENABLED)
            e.getPlayer().getInventory().setItem(4, configuration.QUEUE_JOIN_ITEM);
        else
            LimboBimboPlugin.getPluginInstance().getQueueData().addPlayer(e.getPlayer());
        e.setJoinMessage("");

        ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.networkManager.channel.pipeline().addBefore("packet_handler", e.getPlayer().getName(), packetHandler);
    }

    @EventHandler
    public void playerLoginHandler(PlayerLoginEvent e) {
        if (!e.getAddress().toString().split(":")[0].substring(1).equals(configuration.BUNGEE_IP) && configuration.BUNGEE_ENABLED)
            e.disallow(PlayerLoginEvent.Result.KICK_BANNED, configuration.BUNGEE_MESSAGE.stream().map(ChatUtil::fixColor).collect(Collectors.joining("\n")));
    }

    @EventHandler
    public void playerLeaveHandler(PlayerQuitEvent e) {
        e.setQuitMessage("");
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
    public void interactHandler(PlayerInteractEvent e) {
        if (e.getItem() == null || !configuration.ITEM_ENABLED) return;

        if (e.getItem().isSimilar(configuration.QUEUE_JOIN_ITEM)) {
            LimboBimboPlugin.getPluginInstance().getQueueData().addPlayer(e.getPlayer());
            e.getPlayer().getInventory().clear();
            e.getPlayer().sendMessage(ChatColor.GREEN + "Zostales dodany do kolejki!");
        }
    }
}
