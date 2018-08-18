package jp.thelow.packetChecker;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import jp.thelow.packetChecker.netty.NettyInjectionManager;
import lombok.Getter;

public class PacketCheckerPlugin extends JavaPlugin implements Listener {
  private NettyInjectionManager nettyInjectionManager;

  @Getter
  private static Plugin plguin;

  @Override
  public void onEnable() {
    nettyInjectionManager = new NettyInjectionManager();
    nettyInjectionManager.init();

    plguin = this;

    getServer().getPluginManager().registerEvents(this, this);
  }

  @EventHandler
  public void onPluginDisable(PluginDisableEvent e) throws Exception {
    if (e.getPlugin().equals(this)) {
      for (Player player : Bukkit.getOnlinePlayers()) {
        nettyInjectionManager.remove(player);
      }
    }
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) throws Exception {
    nettyInjectionManager.inject(e.getPlayer());
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e) throws Exception {
    nettyInjectionManager.remove(e.getPlayer());
  }

  @EventHandler
  public void onPluginEnable(PluginEnableEvent e) throws Exception {
    if (e.getPlugin().equals(this)) {
      for (Player player : Bukkit.getOnlinePlayers()) {
        nettyInjectionManager.inject(player);
      }
    }
  }
}
