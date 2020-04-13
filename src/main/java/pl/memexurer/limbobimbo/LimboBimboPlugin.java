package pl.memexurer.limbobimbo;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import pl.memexurer.limbobimbo.config.impl.PluginConfiguration;
import pl.memexurer.limbobimbo.data.AuthenticationData;
import pl.memexurer.limbobimbo.data.queue.LimboQueueData;
import pl.memexurer.limbobimbo.listener.PacketDisablerAdapter;
import pl.memexurer.limbobimbo.listener.PlayerActionListener;
import pl.memexurer.limbobimbo.task.QueueTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class LimboBimboPlugin extends JavaPlugin implements PluginMessageListener {
    private static final PacketType[] DISABLED_PACKETS = new PacketType[]{
            PacketType.Play.Server.PLAYER_INFO,
            PacketType.Play.Server.MAP_CHUNK,
            PacketType.Play.Server.MAP_CHUNK_BULK,
            PacketType.Play.Server.MAP,
            PacketType.Play.Server.ENTITY,
            PacketType.Play.Server.REL_ENTITY_MOVE,
            PacketType.Play.Server.REL_ENTITY_MOVE_LOOK,
            PacketType.Play.Server.ENTITY_DESTROY,
            PacketType.Play.Server.ENTITY_EFFECT,
            PacketType.Play.Server.ENTITY_EQUIPMENT,
            PacketType.Play.Server.ENTITY_HEAD_ROTATION,
            PacketType.Play.Server.ENTITY_LOOK,
            PacketType.Play.Server.ENTITY_METADATA,
            PacketType.Play.Server.ENTITY_STATUS,
            PacketType.Play.Server.ENTITY_TELEPORT,
            PacketType.Play.Server.ENTITY_VELOCITY,
            PacketType.Play.Server.SPAWN_ENTITY,
            PacketType.Play.Server.SPAWN_ENTITY_EXPERIENCE_ORB,
            PacketType.Play.Server.SPAWN_ENTITY_LIVING,
            PacketType.Play.Server.SPAWN_ENTITY_WEATHER,
            PacketType.Play.Server.NAMED_ENTITY_SPAWN};
    private static LimboBimboPlugin PLUGIN_INSTANCE;

    private final PluginConfiguration configuration = new PluginConfiguration(this);
    private final LimboQueueData queueData = new LimboQueueData();
    private final AuthenticationData authenticationData = new AuthenticationData();

    public static LimboBimboPlugin getPluginInstance() {
        return PLUGIN_INSTANCE;
    }

    @Override
    public void onEnable() {
        LimboBimboPlugin.PLUGIN_INSTANCE = this;

        configuration.load();

        Bukkit.getPluginManager().registerEvents(new PlayerActionListener(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new QueueTask(configuration, queueData), 20L, 20L);
        List<PacketType> packets = new ArrayList<>(Collections.singletonList(PacketType.Play.Server.PLAYER_INFO));

        if (configuration.DISABLE_WORLD) packets.addAll(Arrays.asList(DISABLED_PACKETS));
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketDisablerAdapter(this, packets.toArray(new PacketType[]{})));


        this.getServer().getMessenger().registerIncomingPluginChannel(this, "PremiumAuth", this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player huj, byte[] message) {
        ByteArrayDataInput in = ByteStreams.newDataInput(message);

        if (channel.equals("PremiumAuth")) {
            if (in.readUTF().equals("LoginResponse")) {
                authenticationData.authenticatePlayer(in.readUTF());
            }
        }
    }

    public LimboQueueData getQueueData() {
        return queueData;
    }

    public PluginConfiguration getConfiguration() {
        return configuration;
    }

    public AuthenticationData getAuthenticationData() {
        return authenticationData;
    }
}
