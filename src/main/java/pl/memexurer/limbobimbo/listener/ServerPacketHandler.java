package pl.memexurer.limbobimbo.listener;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutKickDisconnect;

import java.util.Arrays;
import java.util.HashSet;

@ChannelHandler.Sharable
public class ServerPacketHandler extends ChannelDuplexHandler {
    private final HashSet<String> deniedPackets = new HashSet<>();
    private final HashSet<String> deniedSentPackets = new HashSet<>();
    private String message;

    ServerPacketHandler() {
        deniedPackets.addAll(Arrays.asList(
                "PacketPlayOutPlayerInfo",
                "PacketPlayOutMapChunk",
                "PacketPlayOutMapChunkBulk",
                "PacketPlayOutNamedSoundEffect",
                "PacketPlayOutTabComplete"
        ));

        deniedSentPackets.addAll(Arrays.asList(
                "PacketPlayInKeepAlive",
                "PacketPlayInBlockPlace",
                "PacketPlayInHeldItemSlot",
                "PacketPlayInCustomPayload"
        ));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
        super.channelRead(ctx, packet);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
        String packetName = packet.getClass().getSimpleName();

        if (this.message != null) {
            super.write(ctx, new PacketPlayOutKickDisconnect(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}")), promise);
            return;
        }

        if (packetName.contains("Entity") || deniedPackets.contains(packetName) && !packetName.equals("PacketPlayOutEntityTeleport") && !packetName.equals("PacketPlayOutCustomPayload"))
            return;
        super.write(ctx, packet, promise);
    }
}
