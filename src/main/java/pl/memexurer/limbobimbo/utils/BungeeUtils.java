package pl.memexurer.limbobimbo.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import pl.memexurer.limbobimbo.LimboBimboPlugin;

public final class BungeeUtils {
    public static void sendPlayer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(player.getName());
        out.writeUTF(server);

        player.sendPluginMessage(LimboBimboPlugin.getPluginInstance(), "BungeeCord", out.toByteArray());
    }
}
