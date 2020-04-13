package pl.memexurer.limbobimbo.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketDisablerAdapter extends PacketAdapter {
    public PacketDisablerAdapter(JavaPlugin plugin, PacketType[] packets) {
        super(plugin, packets);
    }

    @Override
    public void onPacketSending(PacketEvent e) {
        e.setCancelled(true);
    }
}
