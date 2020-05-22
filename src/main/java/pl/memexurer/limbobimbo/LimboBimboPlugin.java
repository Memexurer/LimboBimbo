package pl.memexurer.limbobimbo;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import pl.memexurer.limbobimbo.config.impl.PluginConfiguration;
import pl.memexurer.limbobimbo.data.queue.LimboQueueData;
import pl.memexurer.limbobimbo.listener.PlayerActionListener;
import pl.memexurer.limbobimbo.task.AutoMessageTask;
import pl.memexurer.limbobimbo.task.QueueTask;
import pl.memexurer.limbobimbo.task.ScoreboardTask;

public final class LimboBimboPlugin extends JavaPlugin {
    private static LimboBimboPlugin PLUGIN_INSTANCE;

    private final PluginConfiguration configuration = new PluginConfiguration(this);
    private final LimboQueueData queueData = new LimboQueueData();

    public static LimboBimboPlugin getPluginInstance() {
        return PLUGIN_INSTANCE;
    }

    @Override
    public void onEnable() {
        LimboBimboPlugin.PLUGIN_INSTANCE = this;

        configuration.load();

        Bukkit.getPluginManager().registerEvents(new PlayerActionListener(configuration), this);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new QueueTask(configuration, queueData), 20L, 20L);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        if (configuration.SCOREBOARD_ENABLED)
            Bukkit.getScheduler().runTaskTimer(this, new ScoreboardTask(configuration), 20L, 20L);

        if (configuration.AUTOMSG_ENABLED)
            Bukkit.getScheduler().runTaskTimerAsynchronously(this, new AutoMessageTask(configuration.AUTOMSG_MESSAGES), 0L, configuration.AUTOMSG_INTERVAL * 20L);

        World world = Bukkit.getWorld("world");
        Bukkit.getScheduler().runTaskTimer(this, () -> world.setTime(world.getTime() + 100), 0, 1);
    }

    public LimboQueueData getQueueData() {
        return queueData;
    }

    public PluginConfiguration getConfiguration() {
        return configuration;
    }
}
