package tcc.youajing.teamplugin.services;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import tcc.youajing.teamplugin.entities.MCPlayer;
import tcc.youajing.teamplugin.entities.Team;
import tcc.youajing.teamplugin.utils.MyUtil;

import static tcc.youajing.teamplugin.ObjectPool.pluginConfig;

public class TeamVisitService {
    public boolean setVisit(Player player, Command command, String label, String[] args) {
        // 判断玩家是否在一个团队中
        Team team = TeamManager.getTeamByPlayer(player);
        if (team == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你不在一个团队中！大哥你没有团队咋退出啊！？");
            return true;
        }
        // 判断玩家是否是队长
        if (!team.isPresident(player) && !team.isVicePresident(player)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "只有队长以及副手本人才能取消团队副手！");
            return true;
        }

        // 将玩家现在的位置，作为团队公开的传送点
        TeamManager.setVisit(team, player.getLocation());

        // 发送成功消息给玩家
        player.sendMessage(ChatColor.GREEN + "你已成功设置组织公开传送点");

        return true;
    }

    public boolean visit(Player player, Command command, String label, String[] args) {
        // 获取第二个参数，作为团队名
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team visit <名称>");
            return true;
        }

        // 消耗30级经验
        if(!pluginConfig.debug) {
            if (player.getLevel() < 30) {
                player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你需要至少30级经验才能访问其他组织！谢谢");
                return true;
            } else {
                player.setLevel(player.getLevel() - 30);
            }
        }

        String teamName = args[1];
        // 判断该组织是否存在
        Team team = TeamManager.getTeam(teamName);
        if (team == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "该团队不存在！");
            return true;
        }
        // 判断团队是否有设置公开传送点
        if (team.visit == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "该团队还没有设定公开传送点！请让团队的队长使用 /team setvisit 来设定传送点");
            return true;
        }

        MyUtil.teleport(player, team.visit);

        player.sendMessage(ChatColor.GREEN + "你已成功传送到" + MyUtil.msgColor(team.color) + team.name + ChatColor.GREEN + "！");
        return true;
    }
}
