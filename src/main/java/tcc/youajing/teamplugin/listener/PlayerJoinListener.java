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
import tcc.youajing.teamplugin.services.TeamManager;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void handle(PlayerJoinEvent event) {
        ObjectPool.mcPlayerMapper.update_player(event.getPlayer());
    }
}
