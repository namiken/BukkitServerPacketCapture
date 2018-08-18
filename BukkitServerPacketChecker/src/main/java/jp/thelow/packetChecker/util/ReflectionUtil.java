package jp.thelow.packetChecker.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReflectionUtil {
  public static String getPackageName() {
    String packagename = "net.minecraft.server." + getCraftBukkitVersion();
    return packagename;
  }

  public static String getCraftBukkitVersion() {
    String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    return version;
  }

  public static Class<?> getCraftClass(String s) throws Exception {
    Class<?> craftclass = Class.forName(getPackageName() + "." + s);
    return craftclass;
  }

  public static Object getEntityPlayer(Player p) throws Exception {
    Method getHandle = p.getClass().getMethod("getHandle");
    return getHandle.invoke(p);
  }

  public static Object getFieldValue(Object instance, String fieldName) throws Exception {
    Field field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(instance);
  }

  @SuppressWarnings("unchecked")
  public static <T> T getFieldValue(Field field, Object obj) {
    try {
      return (T) field.get(obj);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Field getField(Class<?> clazz, String fieldName) throws Exception {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field;
  }

  public static void setFieldValue(Field field, Object obj, Object value) {
    try {
      field.set(obj, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
