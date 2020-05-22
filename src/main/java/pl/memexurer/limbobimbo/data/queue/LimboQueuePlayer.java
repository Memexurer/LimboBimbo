package pl.memexurer.limbobimbo.data.queue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.memexurer.limbobimbo.LimboBimboPlugin;
import pl.memexurer.limbobimbo.utils.BungeeUtils;

import java.util.UUID;

public class LimboQueuePlayer implements Comparable<LimboQueuePlayer> {
    private UUID uniqueId;
    private int priority;
    private long connectTime;

    LimboQueuePlayer(Player player) {
        this.priority = LimboBimboPlugin.getPluginInstance().getConfiguration().getPlayerPriorities(player);
        this.uniqueId = player.getUniqueId();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }

    @Override
    public int compareTo(LimboQueuePlayer limboQueuePlayer) {
        return Integer.compare(priority, limboQueuePlayer.priority);
    }

    public long getConnectTime() {
        return (System.currentTimeMillis() - connectTime) / 1000;
    }

    public void connect() {
        this.connectTime = System.currentTimeMillis();
        BungeeUtils.sendPlayer(getPlayer(), LimboBimboPlugin.getPluginInstance().getConfiguration().SERVER_NAME);
    }
}
