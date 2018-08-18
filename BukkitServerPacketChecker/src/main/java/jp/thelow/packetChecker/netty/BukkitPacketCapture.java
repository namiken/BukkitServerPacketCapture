package jp.thelow.packetChecker.netty;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import jp.thelow.packetChecker.PacketCheckerPlugin;
import net.minecraft.server.EnumProtocol;
import net.minecraft.server.EnumProtocolDirection;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;

public class BukkitPacketCapture extends ByteToMessageDecoder {

  private Player player;

  // this packet use nbttag
  // /** capture target packet name */
  // private static final ImmutableSet<String> capturePacketList = ImmutableSet.of("PacketPlayOutTileEntityData",
  // "PacketPlayOutUpdateEntityNBT",
  // "PacketPlayInBlockPlace",
  // "PacketPlayInSetCreativeSlot",
  // "PacketPlayInWindowClick",
  // "PacketPlayOutEntityEquipment",
  // "PacketPlayOutSetSlot",
  // "PacketPlayOutWindowItems",
  // "PacketPlayInCustomPayload");

  public BukkitPacketCapture(Player player) {
    this.player = player;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void decode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, List<Object> list) throws IOException {
    if (bytebuf.readableBytes() != 0) {
      PacketDataSerializer packetdataserializer = new PacketDataSerializer(bytebuf);
      int i = packetdataserializer.e();
      Packet packet = ((EnumProtocol) channelhandlercontext.channel().attr(NetworkManager.c).get()).a(EnumProtocolDirection.SERVERBOUND, i);
      if (packet == null) { return; }

      Logger logger = PacketCheckerPlugin.getPlguin().getLogger();
      logger.log(Level.INFO, MessageFormat.format("PacketLog [{0}] {1}", player.getName(), packet.getClass().getSimpleName()));
    }
  }
}