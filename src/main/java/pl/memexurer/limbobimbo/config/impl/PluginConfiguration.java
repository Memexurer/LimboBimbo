package pl.memexurer.limbobimbo.config.impl;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import pl.memexurer.limbobimbo.config.ConfigurationSource;
import pl.memexurer.limbobimbo.config.CustomConfiguration;
import pl.memexurer.limbobimbo.task.QueueTask;
import pl.memexurer.limbobimbo.utils.ChatUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PluginConfiguration extends CustomConfiguration {
    @ConfigurationSource(path = "queue.messages.awaiting")
    public List<String> QUEUE_MESSAGE_AWAITING;

    @ConfigurationSource(path = "queue.messages.sending")
    public List<String> QUEUE_MESSAGE_SENDING;

    @ConfigurationSource(path = "queue.messages.connecting")
    public List<String> QUEUE_MESSAGE_CONNECTING;

    @ConfigurationSource(path = "queue.messages.connection_failed")
    public List<String> QUEUE_MESSAGE_CONNECTION_FAILED;

    @ConfigurationSource(path = "queue.max_connection_time")
    public int QUEUE_CONNECTION_TIME;

    @ConfigurationSource(path = "disable_world")
    public boolean DISABLE_WORLD;

    @ConfigurationSource(path = "default_gamemode")
    public String DEFAULT_GAMEMODE;

    @ConfigurationSource(path = "server")
    public String SERVER_NAME;

    public ItemStack QUEUE_JOIN_ITEM;

    private final HashMap<String, Integer> QUEUE_PRIORITIES = new HashMap<>();

    public int getPlayerPriorities(Player player) {
        Map.Entry<String, Integer> highestValue = null;

        for (Map.Entry<String, Integer> entry : QUEUE_PRIORITIES.entrySet())
        {
            if(!player.hasPermission(entry.getKey())) continue;
            if (highestValue == null || entry.getValue().compareTo(highestValue.getValue()) > 0) highestValue = entry;
        }

        return highestValue == null ? 1 : highestValue.getValue();
    }

    public PluginConfiguration(JavaPlugin plugin) {
        super(plugin);

        ConfigurationSection section = super.getConfiguration().getConfigurationSection("queue.priorities");
        for(String key: section.getKeys(false))
            QUEUE_PRIORITIES.put(key, section.getInt(key));

        QUEUE_JOIN_ITEM = new ItemStack(Material.getMaterial(super.getConfiguration().getString("item.type")));
        ItemMeta itemMeta = QUEUE_JOIN_ITEM.getItemMeta();
        itemMeta.setDisplayName(ChatUtil.fixColor(super.getConfiguration().getString("item.name")));
        itemMeta.setLore(super.getConfiguration().getStringList("item.name").stream().map(ChatUtil::fixColor).collect(Collectors.toList()));

        QUEUE_JOIN_ITEM.setItemMeta(itemMeta);
    }
}
