//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.services;

import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import tcc.youajing.teamplugin.ObjectPool;
import tcc.youajing.teamplugin.entities.MCPlayer;
import tcc.youajing.teamplugin.entities.Team;

public class TeamInfoService {
    public TeamInfoService() {
    }

    public static boolean info(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "用法: /team info <名称>");
            return true;
        } else {
            Team t = TeamManager.getTeam(args[1]);
            String var10001;
            if (t == null) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "该团队不存在！");
                return true;
            } else {
                String listName = t.getName();
                int size = TeamManager.getSize(t);
                String leaderName = null;
                String fushouName = null;
                if (t.hasVicePresident()) {
                    fushouName = ObjectPool.mcPlayerMapper.get_user_by_uuid(t.vice_president).getName();
                    leaderName = ObjectPool.mcPlayerMapper.get_user_by_uuid(t.president).getName();
                    player.sendMessage(ChatColor.GRAY + "- " + String.valueOf(ChatColor.AQUA) + String.valueOf(ChatColor.BOLD) + listName);
                    player.sendMessage(ChatColor.GRAY + "      [" + size + "人] 队长: " + leaderName + "  副手：" + fushouName);
                } else if (t.hasPresident()) {
                    leaderName = ObjectPool.mcPlayerMapper.get_user_by_uuid(t.president).getName();
                    player.sendMessage(ChatColor.GRAY + "- " + ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + listName);
                    player.sendMessage(ChatColor.GRAY + "      [" + size + "人] 队长: " + leaderName);
                } else {
                    var10001 = String.valueOf(ChatColor.GRAY);
                    player.sendMessage(var10001 + "- " + String.valueOf(ChatColor.AQUA) + String.valueOf(ChatColor.BOLD) + listName);
                }

                StringBuilder msg = new StringBuilder();
                List<MCPlayer> usersByTeam = ObjectPool.mcPlayerMapper.get_users_by_team(t.getName());
                Iterator var11 = usersByTeam.iterator();

                while(var11.hasNext()) {
                    MCPlayer p = (MCPlayer)var11.next();
                    if (!p.getName().equals(leaderName) && !p.getName().equals(fushouName)) {
                        if (msg.length() < 30) {
                            msg.append(p.getName() + ", ");
                        } else {
                            msg.append(p.getName());
                            var10001 = String.valueOf(ChatColor.GOLD);
                            player.sendMessage(var10001 + msg.toString());
                            msg.delete(0, msg.length());
                        }
                    }
                }

                if (msg.length() >= 2) {
                    msg.setLength(msg.length() - 2);
                    var10001 = String.valueOf(ChatColor.GOLD);
                    player.sendMessage(var10001 + msg.toString());
                }

                return true;
            }
        }
    }
}
