package tcc.youajing.teamplugin.services;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import tcc.youajing.teamplugin.entities.Team;

import static tcc.youajing.teamplugin.ObjectPool.mcPlayerMapper;

public class TeamInfoService {
    public static boolean list(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team info <名称>");
        }

        Team t = TeamManager.getTeam(args[1]);
        String listName = t.getName();
        int size = TeamManager.getSize(t);
        String leaderName;
        if (t.hasVicePresident()) {
            String fushouName = mcPlayerMapper.get_user_by_uuid(t.vice_president).getName();
            leaderName = mcPlayerMapper.get_user_by_uuid(t.president).getName();
            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.AQUA + ChatColor.BOLD + listName);
            player.sendMessage(ChatColor.GRAY + "      [" + size + "人] 队长: " + leaderName + "  副手：" + fushouName);
        } else {
            if (t.hasPresident()) {
                leaderName = mcPlayerMapper.get_user_by_uuid(t.president).getName();
                player.sendMessage(ChatColor.GRAY + "- " + ChatColor.AQUA + ChatColor.BOLD + listName);
                player.sendMessage(ChatColor.GRAY + "      [" + size + "人] 队长: " + leaderName);
            } else {
                player.sendMessage(ChatColor.GRAY + "- " + ChatColor.AQUA + ChatColor.BOLD + listName);
            }
        }

        return true;
    }
}
