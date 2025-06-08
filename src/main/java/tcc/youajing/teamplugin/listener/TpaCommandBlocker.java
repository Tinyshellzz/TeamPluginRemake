package tcc.youajing.teamplugin.listener;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import tcc.youajing.teamplugin.config.PluginConfig;

import static tcc.youajing.teamplugin.ObjectPool.pluginConfig;

public class TpaCommandBlocker implements Listener {
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage(); // Full command with leading slash
        Player player = event.getPlayer();

        if (message.toLowerCase().startsWith("/tpa ") || message.toLowerCase().startsWith("/tpahere ")) {
            String target_ = message.split(" ")[1];
            Player target = Bukkit.getServer().getPlayer(target_);
            if (target == null) return;

            double target_playTime = (double) target.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000;
            double player_playTime = (double) player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000;
            if(player_playTime <= pluginConfig.tpa_play_time_requirement) {
                player.sendMessage(ChatColor.RED + "你需要游玩" + pluginConfig.tpa_play_time_requirement + "小时才能使用tpa以及tpahere命令! " + "你还需游玩"+ (pluginConfig.tpa_play_time_requirement-target_playTime) + "小时");
                event.setCancelled(true); // This cancels the command
            } else if(target_playTime <= pluginConfig.tpa_play_time_requirement) {
                player.sendMessage(ChatColor.RED + target.getName() + "需要游玩" + pluginConfig.tpa_play_time_requirement + "小时才能接受你的tpa命令! ");
                event.setCancelled(true); // This cancels the command
            }
        }
    }
}
