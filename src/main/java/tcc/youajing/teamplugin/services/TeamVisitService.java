//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.services;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import tcc.youajing.teamplugin.ObjectPool;
import tcc.youajing.teamplugin.entities.Team;
import tcc.youajing.teamplugin.utils.MyUtil;

public class TeamVisitService {
    public TeamVisitService() {
    }

    public boolean setVisit(Player player, Command command, String label, String[] args) {
        Team team = TeamManager.getTeamByPlayer(player);
        String var10001;
        if (team == null) {
            var10001 = String.valueOf(ChatColor.DARK_RED);
            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你不在一个团队中！大哥你没有团队咋退出啊！？");
            return true;
        } else if (!team.isPresident(player) && !team.isVicePresident(player)) {
            var10001 = String.valueOf(ChatColor.DARK_RED);
            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "只有队长以及副手本人才能取消团队副手！");
            return true;
        } else {
            TeamManager.setVisit(team, player.getLocation());
            player.sendMessage(String.valueOf(ChatColor.GREEN) + "你已成功设置组织公开传送点");
            return true;
        }
    }

    public boolean visit(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "用法: /team visit <名称>");
            return true;
        } else {
            String var10001;
            if (!ObjectPool.pluginConfig.debug && player.getLevel() < 30) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你需要至少30级经验才能访问其他组织！谢谢");
                return true;
            } else {
                String teamName = args[1];
                Team team = TeamManager.getTeam(teamName);
                if (team == null) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "该团队不存在！");
                    return true;
                } else if (team.visit == null) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "该团队还没有设定公开传送点！请让团队的队长使用 /team setvisit 来设定传送点");
                    return true;
                } else {
                    if (!ObjectPool.pluginConfig.debug) {
                        player.setLevel(player.getLevel() - 30);
                    }

                    MyUtil.teleport(player, team.visit);
                    var10001 = String.valueOf(ChatColor.GREEN);
                    player.sendMessage(var10001 + "你已成功传送到" + MyUtil.msgColor(team.color) + team.name + String.valueOf(ChatColor.GREEN) + "！");
                    return true;
                }
            }
        }
    }
}
