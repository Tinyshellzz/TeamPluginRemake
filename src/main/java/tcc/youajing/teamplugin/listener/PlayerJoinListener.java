//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tcc.youajing.teamplugin.ObjectPool;

public class PlayerJoinListener implements Listener {
    public PlayerJoinListener() {
    }

    @EventHandler
    public void handle(PlayerJoinEvent event) {
        Bukkit.getConsoleSender().sendMessage("PlayerJoinListener.handle get_user_by_uuid" + String.valueOf(event.getPlayer()));
        ObjectPool.mcPlayerMapper.update_player(event.getPlayer());
    }
}
