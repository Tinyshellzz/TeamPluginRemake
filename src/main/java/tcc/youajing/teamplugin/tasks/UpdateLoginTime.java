package tcc.youajing.teamplugin.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import static tcc.youajing.teamplugin.ObjectPool.mcPlayerMapper;

public class UpdateLoginTime implements Runnable {
    public void run() {
        Bukkit.getConsoleSender().sendMessage("TeamPlugin: Updating login time");
        mcPlayerMapper.update_players();
    }
}
