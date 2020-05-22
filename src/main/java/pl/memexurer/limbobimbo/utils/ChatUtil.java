package pl.memexurer.limbobimbo.utils;

import net.minecraft.server.v1_8_R3.ChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class ChatUtil {
    public static String fixColor(String str) {
        return ChatColor.translateAlternateColorCodes('&', str.replace(">>", "\u00BB").replace("<<", "\u00AB"));
    }

    public static void sendActionBar(Player player, String text) {
        PacketPlayOutChat chat = new PacketPlayOutChat(ChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatUtil.fixColor(text) + "\"}"), (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(chat);
    }
}
