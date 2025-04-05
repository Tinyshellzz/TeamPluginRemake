//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

public class MyUtil {
    private static final Pattern colorPattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");

    public MyUtil() {
    }

    public static String msgColor(String colorCode) {
        Matcher matcher = colorPattern.matcher(colorCode);
        String colorStr = "";
        if (matcher.find()) {
            colorStr = ChatColor.of(colorCode).toString();
        }

        return ChatColor.translateAlternateColorCodes('&', colorStr);
    }

    public static String msgColor2(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void teleport(Player player, Location location) {
        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

        try {
            Class<?> pluginMetaClass = Class.forName("io.papermc.paper.plugin.configuration.PluginMeta");
            pluginMetaClass.getMethod("isFoliaSupported");
            player.teleportAsync(location, TeleportCause.PLUGIN);
        } catch (ClassNotFoundException var3) {
            player.teleport(location, TeleportCause.PLUGIN);
        } catch (NoSuchMethodException var4) {
            player.teleport(location, TeleportCause.PLUGIN);
        }

    }

    public static boolean isOnline(UUID uuid) {
        return Bukkit.getPlayer(uuid) != null;
    }

    public boolean isPlaceHolderAPIInstalled() {
        Plugin placeholderAPI = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI");
        return placeholderAPI != null && placeholderAPI.isEnabled();
    }
}
