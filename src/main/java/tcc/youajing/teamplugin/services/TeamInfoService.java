package tcc.youajing.teamplugin.services;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import tcc.youajing.teamplugin.database.MCPlayerMapper;
import tcc.youajing.teamplugin.entities.MCPlayer;
import tcc.youajing.teamplugin.entities.Team;

import java.util.List;

import static tcc.youajing.teamplugin.ObjectPool.mcPlayerMapper;

public class TeamInfoService {
    public static boolean info(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team info <名称>");
        }

        Team t = TeamManager.getTeam(args[1]);
        if (t == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "该团队不存在！");
            return true;
        }

        String listName = t.getName();
        int size = TeamManager.getSize(t);
        String leaderName = null;
        String fushouName = null;
        if (t.hasVicePresident()) {
            fushouName = mcPlayerMapper.get_user_by_uuid(t.vice_president).getName();
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

        StringBuilder msg = new StringBuilder();
        List<MCPlayer> usersByTeam = mcPlayerMapper.get_users_by_team(t.getName());
        for(MCPlayer p : usersByTeam) {
            if(!p.getName().equals(leaderName) && !p.getName().equals(fushouName)) {
                if (msg.length() < 30) {
                    msg.append(p.getName() + ", ");
                } else {
                    msg.append(p.getName());
                    player.sendMessage(ChatColor.GOLD + msg.toString());
                    msg.delete(0, msg.length());
                }
            }
        }
        if(msg.length() >= 2) {
            msg.setLength(msg.length() - 2);
            player.sendMessage(ChatColor.GOLD + msg.toString());
        }

        return true;
    }
}
