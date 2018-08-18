package jp.thelow.packetChecker.netty;

import java.lang.reflect.Field;

import org.bukkit.entity.Player;

import io.netty.channel.Channel;
import jp.thelow.packetChecker.util.ReflectionUtil;

public class NettyInjectionManager {

  private Class<?> entityPlayerClass;
  private Field playerConnectionField;

  private Class<?> playerConnectionClass;
  private Field networkManagerField;

  private Class<?> networkManagerClass;
  private Field channelField;

  public void init() {
    try {

      entityPlayerClass = ReflectionUtil.getCraftClass("EntityPlayer");
      playerConnectionField = ReflectionUtil.getField(entityPlayerClass, "playerConnection");

      playerConnectionClass = ReflectionUtil.getCraftClass("PlayerConnection");
      networkManagerField = ReflectionUtil.getField(playerConnectionClass, "networkManager");

      networkManagerClass = ReflectionUtil.getCraftClass("NetworkManager");

      // find Channel Type Field
      Field[] declaredFields = networkManagerClass.getDeclaredFields();
      for (Field field : declaredFields) {
        Class<?> type = field.getType();
        if (type.isAssignableFrom(Channel.class)) {
          field.setAccessible(true);
          channelField = field;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void inject(Player player) throws Exception {
    Object entityPlayer = ReflectionUtil.getEntityPlayer(player);
    Object networkManager = getNetworkManager(entityPlayer);
    Channel channel = getChannel(networkManager);
    BukkitPacketCapture pch = new BukkitPacketCapture(player);
    if (channel.pipeline().get(BukkitPacketCapture.class) == null) {
      channel.pipeline().addBefore("decoder", "packetCapture", pch);
    }
  }

  public void remove(final Player player) throws Exception {
    Object entityPlayer = ReflectionUtil.getEntityPlayer(player);
    Object networkManager = getNetworkManager(entityPlayer);
    final Channel channel = getChannel(networkManager);
    if (channel.pipeline().get(BukkitPacketCapture.class) != null) {
      channel.pipeline().remove(BukkitPacketCapture.class);
    }
  }

  private Object getNetworkManager(Object entityPlayer) {
    Object pc = ReflectionUtil.getFieldValue(playerConnectionField, entityPlayer);
    return ReflectionUtil.getFieldValue(networkManagerField, pc);
  }

  private Channel getChannel(Object networkManager) {
    return ReflectionUtil.getFieldValue(channelField, networkManager);
  }
}
