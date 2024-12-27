package tcc.youajing.teamplugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tcc.youajing.teamplugin.entities.LoginTime;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static tcc.youajing.teamplugin.ObjectPool.gson;
import static tcc.youajing.teamplugin.ObjectPool.mcPlayerMapper;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void handle(PlayerJoinEvent event) {
        mcPlayerMapper.update_player(event.getPlayer());
    }
}

